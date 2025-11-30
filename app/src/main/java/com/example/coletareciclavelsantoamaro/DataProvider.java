package com.example.coletareciclavelsantoamaro;

import java.util.*;
import java.util.Calendar;

public class DataProvider {

    private static final String[] BAIRROS = new String[]{
            "São Francisco",
            "Loteamento Tereza Cristina",
            "São Dimas",
            "São Pedro",
            "Centro (parte Nova Brasília/Vargem Grande)",
            "Vargem Grande e Lambari",
            "Bairro São Francisco",
            "Vila Becker",
            "Cohab",
            "Centro (até rua Blumenau)",
            "Caminho do Rei",
            "Pontal dos Cardosos",
            "Sertão dos Corrêas",
            "Bairro São João",
            "Sul do Rio",
            "Caminho Novo",
            "Avenida Ângelo Cassetari Vieira",
            "Centro (parte do Vale Verde)",
            "Vargem Grande",
            "Urubici",
            "Centro"
    };

    private static final Map<String, String[]> RECICLADO_LINHAS = new LinkedHashMap<>();
    private static final Map<String, int[]>   RECICLADO_WEEKDAYS = new LinkedHashMap<>();
    static {
        // Segunda
        put("São Francisco", new String[]{"segunda","sexta"});
        put("Loteamento Tereza Cristina", new String[]{"segunda"});
        put("São Dimas", new String[]{"segunda"});
        put("São Pedro", new String[]{"segunda"});
        put("Centro (parte Nova Brasília/Vargem Grande)", new String[]{"segunda"});
        put("Vargem Grande e Lambari", new String[]{"segunda"});
        put("Bairro São Francisco", new String[]{"segunda"});
        put("Vila Becker", new String[]{"segunda"});

        // Terça
        put("Cohab", new String[]{"terça"});
        put("Centro (até rua Blumenau)", new String[]{"terça"});
        put("Caminho do Rei", new String[]{"terça"});
        put("Pontal dos Cardosos", new String[]{"terça"});
        put("Sertão dos Corrêas", new String[]{"terça","quinta","sexta"});
        put("Bairro São João", new String[]{"terça","quinta","sexta"});

        // Quinta
        put("Sul do Rio", new String[]{"quinta","sexta","sábado"});
        put("Caminho Novo", new String[]{"quinta","sábado"});
        put("Avenida Ângelo Cassetari Vieira", new String[]{"quinta"});

        // Sexta
        put("Centro (parte do Vale Verde)", new String[]{"sexta"});
        put("Vargem Grande", new String[]{"sexta","sábado"});

        // Sábado
        put("Urubici", new String[]{"sábado"});
        put("Centro", new String[]{"sábado"});
    }

    private static void put(String bairro, String[] diasPT){
        RECICLADO_LINHAS.put(bairro, diasPT.clone());
        int[] w = new int[diasPT.length];
        for (int i=0;i<diasPT.length;i++){
            switch (diasPT[i]) {
                case "segunda": w[i]=Calendar.MONDAY; break;
                case "terça":   w[i]=Calendar.TUESDAY; break;
                case "quarta":  w[i]=Calendar.WEDNESDAY; break;
                case "quinta":  w[i]=Calendar.THURSDAY; break;
                case "sexta":   w[i]=Calendar.FRIDAY; break;
                case "sábado":  w[i]=Calendar.SATURDAY; break;
                case "domingo": w[i]=Calendar.SUNDAY; break;
            }
        }
        RECICLADO_WEEKDAYS.put(bairro, w);
    }

    public static List<String> getBairros(){
        List<String> lista = new ArrayList<>(Arrays.asList(BAIRROS));
        Collections.sort(lista, String.CASE_INSENSITIVE_ORDER);
        return lista;
    }

    public static String[] getRecicladoLinhas(String bairro){
        String[] v = RECICLADO_LINHAS.get(bairro);
        return v != null ? v : new String[]{"Sem programação para este bairro."};
    }

    public static int[] getRecicladoWeekdays(String bairro){
        int[] v = RECICLADO_WEEKDAYS.get(bairro);
        return v != null ? v : new int[]{};
    }
}
