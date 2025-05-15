package com.dcc;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

// Analisador customizado para inglês com stemming
public class EnglishAnalyzerWithStemming extends Analyzer {
    private final CharArraySet stopWords;

    public EnglishAnalyzerWithStemming() {
        // Usa as stopwords padrão do EnglishAnalyzer
        this.stopWords = EnglishAnalyzer.ENGLISH_STOP_WORDS_SET;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer source = new StandardTokenizer();
        TokenStream result = new LowerCaseFilter(source);
        result = new StopFilter(result, stopWords); // Remove stopwords
        result = new PorterStemFilter(result);      // Aplica stemming
        return new TokenStreamComponents(source, result);
    }
}