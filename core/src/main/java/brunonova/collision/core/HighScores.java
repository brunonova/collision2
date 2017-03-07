/*
 * Copyright (C) 2017 Bruno Nova <brunomb.nova@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package brunonova.collision.core;

import brunonova.collision.core.enums.Difficulty;
import brunonova.collision.core.enums.GameMode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the game's high scores.
 * <p>The scores are stored in the {@code scores.xml} preferences XML file.</p>
 * <p>Each line specifies the high score, with the player name and score, for a
 * specific game mode, difficulty and index (a lower index means a higher
 * score).</p>
 * <p>The key of each line is composed by the game mode, the difficulty and
 * the index, in this order and separated by underscores ('_').</p>
 * <p>The value of each line is composed by the score and the name of the
 * player, in this order and separated by a colon (':'), encoded in Base64.</p>
 */
public final class HighScores {
    private static final String TAG = HighScores.class.getName();

    /** Maximum number of high scores per game mode and difficulty. */
    public static final int MAX_SCORES = 5;

    /** The high scores. */
    private Map<GameMode, Map<Difficulty, List<Score>>> highScores;
    /** The scores Preferences object. */
    private Preferences scoresPref;
    /** The game. */
    private final Collision game;

    /**
     * Creates this object and loads the high scores.
     * @param game The game.
     */
    public HighScores(Collision game) {
        this.game = game;
        loadScores();
    }

    /**
     * Loads the high scores from the preferences file.
     */
    public synchronized void loadScores() {
        // Get the scores Preferences object
        scoresPref = Gdx.app.getPreferences("scores.xml");

        // Loop through all game modes
        highScores = new HashMap<>();
        for(GameMode mode: GameMode.values()) {
            Map<Difficulty, List<Score>> difficultiesMap = new HashMap<>();
            highScores.put(mode, difficultiesMap);

            // Loop through all dificulties
            for(Difficulty difficulty: Difficulty.values()) {
                List<Score> scoresList = new ArrayList<>();
                difficultiesMap.put(difficulty, scoresList);

                // Load all scores for this game mode and difficulty
                for(int i = 0; i < MAX_SCORES; i++) {
                    Score score = getScoreFromPref(mode, difficulty, i);
                    if(score != null) {
                        scoresList.add(score);
                    } else {
                        // No more scores exist for this mode and difficulty
                        break;
                    }
                }
            }
        }
    }

    /**
     * Saves the high scores into the preferences file.
     */
    public synchronized void saveScores() {
        // Clear saved high scores.
        scoresPref.clear();

        // Loop through all game modes
        for(GameMode mode: GameMode.values()) {
            // Loop through all dificulties
            for(Difficulty difficulty: Difficulty.values()) {
                List<Score> scores = getScores(mode, difficulty);

                // Save all scores for this game mode and difficulty
                for(int i = 0; i < scores.size(); i++) {
                    putScoreIntoPref(mode, difficulty, i, scores.get(i));
                }
            }
        }

        // Write to file
        scoresPref.flush();
    }

    /**
     * Returns the high scores for the specified game mode and difficulty.
     * @param mode The game mode.
     * @param difficulty The difficulty.
     * @return The high scores, ordered by score, descending.
     */
    public synchronized List<Score> getScores(GameMode mode, Difficulty difficulty) {
        return highScores.get(mode).get(difficulty);
    }

    /**
     * Adds the specified score to the list of high scores for the specified
     * game mode and difficulty, then saves the scores to the preferences.
     * @param mode The game mode.
     * @param difficulty The difficulty.
     * @param name The name of the player.
     * @param score The score.
     * @return The index in the list where this score was added, or -1 if it
     *         wasn't added (it isn't a high score).
     */
    public synchronized int addScore(GameMode mode, Difficulty difficulty, String name, int score) {
        List<Score> scores = getScores(mode, difficulty);
        Score newScore = new Score(name, score);
        int index = -1;

        // Find the first high score that is worse than this one, if any
        for(int i = 0; i < scores.size(); i++) {
            if(score > scores.get(i).getScore()) {
                scores.add(i, newScore);
                index = i;
                break;
            }
        }

        // No high score is worse that this one? Then add the score to the end
        // of the list, if possible
        if(index < 0) {
            // Is there still space for a new high score?
            if(scores.size() < MAX_SCORES) {
                scores.add(newScore);
                index = scores.size();
            } else {
                // No space. This is not an high score
                return -1;
            }
        }

        // Keep the list of high scores inside the limit
        while(scores.size() > MAX_SCORES) {
            scores.remove(scores.size() - 1);
        }

        // Save the scores to the preferences, asynchronously
        if(index >= 0) {
            game.getAsyncExecutor().submit(() -> {
                saveScores();
                return true;
            });
        }

        return index;
    }

    /**
     * Returns whether the specified score can enter the table of high scores
     * (i.e. if it's high enough for the specified game mode and difficulty).
     * @param mode The game mode.
     * @param difficulty The difficulty.
     * @param score The score.
     * @return {@code true} if it's a high score.
     */
    public synchronized boolean isHighScore(GameMode mode, Difficulty difficulty, int score) {
        if(score <= 0) {
            // A score of 0 is not an high score
            return false;
        } else {
            List<Score> scores = getScores(mode, difficulty);
            if(scores.size() < MAX_SCORES) {
                // Limit of high scores not reached yet
                return true;
            } else {
                // Check if this score is better than the last high score
                return score > scores.get(scores.size() - 1).getScore();
            }
        }
    }

    /**
     * Returns the high score for the specified game mode, difficulty and index
     * from the preferences.
     * @param mode The game mode.
     * @param difficulty The difficulty.
     * @param index The index in the list.
     * @return The score, or {@code null} if it doesn't exist or is invalid.
     */
    private Score getScoreFromPref(GameMode mode, Difficulty difficulty, int index) {
        // Construct the key and get the value from the preferences
        String key = mode + "_" + difficulty + "_" + index;
        String value = scoresPref.getString(key);

        if(value != null && !value.isEmpty()) {
            // Decode the value from Base64
            try {
                value = new String(Base64.getDecoder().decode(value.getBytes("UTF-8")), "UTF-8");
            } catch(UnsupportedEncodingException ex) {
                Gdx.app.error(TAG, "Error decoding score", ex);
                return null;
            }

            // Extract the name and score from the value ("score:name")
            String[] fields = value.split(":", 2);
            if(fields.length == 2) {
                try {
                    // Create and return the Score object
                    int score = Integer.parseInt(fields[0]);
                    String name = fields[1];
                    return new Score(name, score);
                } catch(NumberFormatException ex) {
                    // The score is not an integer
                    Gdx.app.error(TAG, "Error converting the score for " + key
                            + " into an integer", ex);
                    return null;
                }
            } else {
                // Wrong number of fields
                Gdx.app.error(TAG, "The score for " + key
                        + " doesn't have the expected number of fields. "
                        + "Expected 2, got " + fields.length);
                return null;
            }
        } else {
            // No score for this mode, difficulty and index
            return null;
        }
    }

    /**
     * Puts the specified score in the preferences.
     * @param mode The game mode.
     * @param difficulty The difficulty.
     * @param index The index in the list.
     * @param score The score.
     */
    private void putScoreIntoPref(GameMode mode, Difficulty difficulty, int index, Score score) {
        // Construct the key
        String key = mode + "_" + difficulty + "_" + index;

        // Constructs and encodes the value in Base64
        String value = score.toString();
        try {
            value = new String(Base64.getEncoder().encode(value.getBytes("UTF-8")), "UTF-8");
        } catch(UnsupportedEncodingException ex) {
            Gdx.app.error(TAG, "Error encoding score", ex);
            return;
        }

        // Store the score in the preferences
        scoresPref.putString(key, value);
    }


    /**
     * Represents an high score.
     */
    public static class Score {
        private final String name;
        private final int score;

        /**
         * Creates the high score.
         * @param name The name of the player
         * @param score The score.
         */
        public Score(String name, int score) {
            this.name = name;
            this.score = score;
        }

        /**
         * Returns the name of the player.
         * @return The name of the player.
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the score.
         * @return The score.
         */
        public int getScore() {
            return score;
        }

        @Override
        public String toString() {
            return score + ":" + name;
        }
    }
}
