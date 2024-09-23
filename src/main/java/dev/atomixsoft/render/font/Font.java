package dev.atomixsoft.render.font;

import dev.atomixsoft.render.Texture;
import dev.atomixsoft.render.render2D.Sprite;
import dev.atomixsoft.render.render2D.SpriteBatch;

public class Font {

    private final Texture m_Atlas;
    private final Glyph[] m_Glyphs;

    public Font(String filePath) {
        m_Atlas = new Texture(filePath);
        m_Glyphs = new Glyph[61];
    }

    private void createGlyphs(int cellWidth, int cellHeight) {
        // Characters displayed in the image from top-left to bottom-right
        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-=;!@#$%^&*()_+:/\\,.?'\"| ";

        // Ensure that the glyph array size matches the number of valid characters
        if (validCharacters.length() > m_Glyphs.length) {
            throw new IllegalArgumentException("Glyphs array size is too small.");
        }

        // Calculate the number of columns (13) based on the image dimensions
        int columns = 13; // 13 characters per row

        // Loop over the valid characters and assign glyphs
        for (int i = 0; i < validCharacters.length(); ++i) {
            char c = validCharacters.charAt(i);

            // Determine the glyph's position in the texture based on its row and column
            int col = i % columns; // x-axis index in the grid
            int row = i / columns; // y-axis index in the grid

            // Create a new Sprite for each glyph
            Sprite sprite = new Sprite(m_Atlas);
            sprite.setSize(16, 16);

            // Set the correct cell position for the sprite
            sprite.setCellPos(col * cellWidth, row * cellHeight);
            sprite.setCellSize(cellWidth, cellHeight);

            // Create a new Glyph and store it
            Glyph glyph = new Glyph();
            glyph.id = Character.toString(c);
            glyph.sprite = sprite;

            m_Glyphs[i] = glyph;
        }
    }

    public void drawText(SpriteBatch batch, String msg, float x, float y) {
        drawText(batch, msg, x, y, 8f);
    }

    public void drawText(SpriteBatch batch, String msg, float x, float y, float size) {
        x += 8;
        y += 12;

        int line = 1;
        for(var s = 0; s < msg.length(); ++s) {
            if(Character.toString(msg.charAt(s)).equalsIgnoreCase("\n"))
                line++;
        }

        float oX = 0;
        for(var c = 0; c < msg.length(); c++) {
            String ch = Character.toString(msg.charAt(c));

            if(ch.equalsIgnoreCase("\n")) {
                oX = 0;
                line--;
                continue;
            }

            Glyph glyph = null;
            for(Glyph g : m_Glyphs) {
                if(g.getId().equalsIgnoreCase(ch)) {
                    glyph = g;
                    break;
                }
            }
            assert glyph != null;

            glyph.getSprite().setSize(size, size);

            switch (ch) {
                case ":", ";", ",", "." -> glyph.getSprite().setPosition(x + (oX * glyph.getSprite().getSize().x / 1.75f), y * line + (glyph.getSprite().getSize().y / 2f) - 1.5f, 0.1f);
                default -> glyph.getSprite().setPosition(x + (oX * glyph.getSprite().getSize().x / 1.75f), y * line + (glyph.getSprite().getSize().y / 2f) , 0.1f);
            }

            glyph.getSprite().draw(batch);
            oX+=1.25f;
        }
    }

    public Glyph[] getGlyphs() {
        return m_Glyphs;
    }

    public static Font createFont(String path, int cellWidth, int cellHeight) {
        Font font = new Font(path);
        font.createGlyphs(cellWidth, cellHeight);
        return font;
    }

    public static class Glyph {
        private String id;
        private Sprite sprite;

        public String getId() { return id; }
        public Sprite getSprite() { return sprite; }
    }

}
