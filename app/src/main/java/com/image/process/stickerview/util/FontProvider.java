package com.image.process.stickerview.util;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * extracting Typeface from Assets is a heavy operation,
 * we want to make sure that we cache the typefaces for reuse
 */
public class FontProvider {

    private static final String DEFAULT_FONT_NAME = "Helvetica";

    private final LinkedHashMap<String, Typeface> typefaces;
    private final LinkedHashMap<String, String> fontNameToTypefaceFile;
    private final Resources resources;
    private final List<String> fontNames;

    public FontProvider(Resources resources) {
        this.resources = resources;

        typefaces = new LinkedHashMap<>();

        // populate fonts
        fontNameToTypefaceFile = new LinkedHashMap<>();
        fontNameToTypefaceFile.put("Sample Font Arya Bold", "Arya-Bold.ttf");
        fontNameToTypefaceFile.put("Sample Font Halant Bold", "Halant-Bold.ttf");
        fontNameToTypefaceFile.put("Sample Font Hind Bold", "Hind-Bold.ttf");
        fontNameToTypefaceFile.put("Sample Font SansDevanagari Bold", "IBMPlexSansDevanagari-Bold.ttf");
        fontNameToTypefaceFile.put("Sample Font Inknut Antiqua Black", "InknutAntiqua-Black.ttf");
        fontNameToTypefaceFile.put("Sample Font Inknut Antiqua ExtraBold", "InknutAntiqua-ExtraBold.ttf");
        fontNameToTypefaceFile.put("Sample Font Kalam Bold", "Kalam-Bold.ttf");
        fontNameToTypefaceFile.put("Sample Font khand bold", "khand-bold.ttf");
        fontNameToTypefaceFile.put("Sample Font Khula ExtraBold", "Khula-ExtraBold.ttf");
        fontNameToTypefaceFile.put("Sample Font NotoSans Bold", "NotoSans-Bold.ttf");
        fontNameToTypefaceFile.put("Sample Font PalanquinDark SemiBold", "PalanquinDark-SemiBold.ttf");
        fontNameToTypefaceFile.put("Sample Font Rajdhani Bold", "Rajdhani-Bold.ttf");
        fontNameToTypefaceFile.put("Sample Font Teko Bold", "Teko-Bold.ttf");
        fontNameToTypefaceFile.put("Sample Font Teko SemiBold", "Teko-SemiBold.ttf");
        fontNameToTypefaceFile.put("Sample Font Tillana Bold", "Tillana-Bold.ttf");
        fontNameToTypefaceFile.put("Sample Font Yantramanav Black", "Yantramanav-Black.ttf");
        fontNameToTypefaceFile.put("Sample Font Yantramanav Bold", "Yantramanav-Bold.ttf");
        fontNameToTypefaceFile.put("Sample Font devnew", "devnew.ttf");
        fontNameToTypefaceFile.put("Sample Font Roupya", "roupya.ttf");
        fontNameToTypefaceFile.put("Sample Font sharada", "sharada.TTF");
        fontNameToTypefaceFile.put("Sample Font kanak", "kanak.TTF");
        fontNameToTypefaceFile.put("Sample Font saras", "saras.TTF");
        fontNameToTypefaceFile.put("Sample Font vakra", "vakra.ttf");
        fontNameToTypefaceFile.put("Sample Font SAMAN", "SAMAN.ttf");
        fontNameToTypefaceFile.put("Sample Font Indian Calligra", "bitsindiancalligra-Regular.ttf");
        fontNameToTypefaceFile.put("Sample Font Mi Marathi", "mimarathi.ttf");
        fontNameToTypefaceFile.put("Sample Font Grinched", "Grinched.ttf");
        fontNameToTypefaceFile.put("Sample Font Aka Dora", "akaDora.ttf");
        fontNameToTypefaceFile.put("Sample Sarpanch ExtraBold", "Sarpanch-ExtraBold.ttf");
        fontNameToTypefaceFile.put("Sample Amita Bold", "Amita-Bold.ttf");
        fontNameToTypefaceFile.put("Sample Poppins ExtraBold", "Poppins-ExtraBold.ttf");
        fontNameToTypefaceFile.put("Sample RozhaOne Regular", "RozhaOne-Regular.ttf");
        fontNameToTypefaceFile.put("Sample Eczar SemiBold", "Eczar-SemiBold.ttf");
        fontNameToTypefaceFile.put("Sample Eczar-ExtraBold", "Eczar-ExtraBold.ttf");
        fontNameToTypefaceFile.put("Sample Laila-Bold", "Laila-Bold.ttf");



        fontNames = new ArrayList<>(fontNameToTypefaceFile.keySet());
    }

    /**
     * @param typefaceName must be one of the font names provided from {@link FontProvider#getFontNames()}
     * @return the Typeface associated with {@code typefaceName}, or {@link Typeface#DEFAULT} otherwise
     */
    public Typeface getTypeface(@Nullable String typefaceName) {
        if (TextUtils.isEmpty(typefaceName)) {
            return Typeface.DEFAULT;
        } else {
            //noinspection Java8CollectionsApi
            if (typefaces.get(typefaceName) == null) {
                typefaces.put(typefaceName,
                        Typeface.createFromAsset(resources.getAssets(), "fonts/" + fontNameToTypefaceFile.get(typefaceName)));
            }
            return typefaces.get(typefaceName);
        }
    }

    /**
     * use {@link FontProvider#getTypeface(String) to get Typeface for the font name}
     *
     * @return list of available font names
     */
    public List<String> getFontNames() {
        return fontNames;
    }

    /**
     * @return Default Font Name - <b>Helvetica</b>
     */
    public String getDefaultFontName() {
        return DEFAULT_FONT_NAME;
    }
}