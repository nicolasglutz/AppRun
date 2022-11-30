package ch.morseencoder.morse;

import static ch.morseencoder.morse.Primitive.DAH;
import static ch.morseencoder.morse.Primitive.DIT;
import static ch.morseencoder.morse.Primitive.GAP;
import static ch.morseencoder.morse.Primitive.SYMBOL_GAP;
import static ch.morseencoder.morse.Primitive.WORD_GAP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MorseEncoder {
    private static Map<Character, Primitive[]> characterToSymbolMap = new HashMap<>();

    static {
        characterToSymbolMap.put('A', new Primitive[]{DIT, GAP, DAH});
        characterToSymbolMap.put('B', new Primitive[]{DAH, GAP, DIT, GAP, DIT, GAP, DIT});
        characterToSymbolMap.put('C', new Primitive[]{DAH, GAP, DIT, GAP, DAH, GAP, DIT});
        characterToSymbolMap.put('D', new Primitive[]{DAH, GAP, DIT, GAP, DIT});
        characterToSymbolMap.put('E', new Primitive[]{DIT});
        characterToSymbolMap.put('F', new Primitive[]{DIT, GAP, DIT, GAP, DAH, GAP, DIT});
        characterToSymbolMap.put('G', new Primitive[]{DAH, GAP, DAH, GAP, DIT});
        characterToSymbolMap.put('H', new Primitive[]{DIT, GAP, DIT, GAP, DIT, GAP, DIT});
        characterToSymbolMap.put('I', new Primitive[]{DIT, GAP, DIT});
        characterToSymbolMap.put('J', new Primitive[]{DIT, GAP, DAH, GAP, DAH, GAP, DAH});
        characterToSymbolMap.put('K', new Primitive[]{DAH, GAP, DIT, GAP, DAH});
        characterToSymbolMap.put('L', new Primitive[]{DIT, GAP, DAH, GAP, DIT, GAP, DIT});
        characterToSymbolMap.put('M', new Primitive[]{DAH, GAP, DAH});
        characterToSymbolMap.put('N', new Primitive[]{DAH, GAP, DIT});
        characterToSymbolMap.put('O', new Primitive[]{DAH, GAP, DAH, GAP, DAH});
        characterToSymbolMap.put('P', new Primitive[]{DIT, GAP, DAH, GAP, DAH, GAP, DIT});
        characterToSymbolMap.put('Q', new Primitive[]{DAH, GAP, DAH, GAP, DIT, GAP, DAH});
        characterToSymbolMap.put('R', new Primitive[]{DIT, GAP, DAH, GAP, DIT});
        characterToSymbolMap.put('S', new Primitive[]{DIT, GAP, DIT, GAP, DIT});
        characterToSymbolMap.put('T', new Primitive[]{DAH});
        characterToSymbolMap.put('U', new Primitive[]{DIT, GAP, DIT, GAP, DAH});
        characterToSymbolMap.put('V', new Primitive[]{DIT, GAP, DIT, GAP, DIT, GAP, DAH});
        characterToSymbolMap.put('W', new Primitive[]{DIT, GAP, DAH, GAP, DAH});
        characterToSymbolMap.put('X', new Primitive[]{DAH, GAP, DIT, GAP, DIT, GAP, DAH});
        characterToSymbolMap.put('Y', new Primitive[]{DAH, GAP, DIT, GAP, DAH, GAP, DAH});
        characterToSymbolMap.put('Z', new Primitive[]{DAH, GAP, DAH, GAP, DIT, GAP, DIT});
        characterToSymbolMap.put(' ', new Primitive[]{WORD_GAP});
    }

    public List<Primitive> textToCode(String text) throws Exception {
        List<Primitive> code = new ArrayList<>();

        for (int characterIndex = 0; characterIndex < text.length(); ++characterIndex) {
            char character = text.charAt(characterIndex);

            Primitive[] symbol = characterToSymbol(character);

            // add space between symbols if necessary
            if (code.size() > 0 && code.get(code.size() - 1).isLightOn() && symbol[0].isLightOn()) {
                code.add(SYMBOL_GAP);
            }

            code.addAll(Arrays.asList(symbol));
        }

        return code;
    }

    private Primitive[] characterToSymbol(Character character) throws Exception {
        Primitive[] symbol = characterToSymbolMap.get(character);

        if (symbol == null) {
            throw new Exception("unknown character " + character);
        }

        return symbol;
    }
}
