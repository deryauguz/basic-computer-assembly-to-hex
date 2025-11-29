package _23010310075_Esma_Uğuz;

import java.io.*;
import java.util.*;

public class _23010310075_Esma_Uğuz {

    public static void main(String[] args) {

        ArrayList<Komutlar> komutListesi = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("Program.txt"));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.equals("")) continue;

                String[] parts = line.split("\\s+");

                String adres = parts[0];
                String islem = parts[1];
                String hafizaAdresi = null;
                boolean iBiti = false;

                if (parts.length >= 3) hafizaAdresi = parts[2];
                if (parts.length == 4 && parts[3].equals("I")) iBiti = true;

                komutListesi.add(new Komutlar(adres, islem, hafizaAdresi, iBiti));
            }

            bufferedReader.close();

        } catch (Exception e) {
            System.out.println("Dosya okuma hatasi!");
        }

        Assembly derleyici = new Assembly(komutListesi);
        derleyici.calistir();
    }
}

class Komutlar {
    String adres;
    String islem;
    String hafizaAdresi;
    boolean iBiti;

    public Komutlar(String adres, String islem, String hafizaAdresi, boolean iBiti) {
        this.adres = adres;
        this.islem = islem;
        this.hafizaAdresi = hafizaAdresi;
        this.iBiti = iBiti;
    }
}

class Assembly {

    ArrayList<Komutlar> komutListesi;
    HashMap<String, String> map = new HashMap<>();

    public Assembly(ArrayList<Komutlar> komutListesi) {
        this.komutListesi = komutListesi;
        opcodeMapOlustur();
    }

    void opcodeMapOlustur() {
        map.put("AND", "0");
        map.put("ADD", "1");
        map.put("LDA", "2");
        map.put("STA", "3");
        map.put("BUN", "4");
        map.put("BSA", "5");
        map.put("ISZ", "6");

        map.put("CLA", "7800");
        map.put("CLE", "7400");
        map.put("CMA", "7200");
        map.put("CME", "7100");
        map.put("CIR", "7080");
        map.put("CIL", "7040");
        map.put("INC", "7020");
        map.put("SPA", "7010");
        map.put("SNA", "7008");
        map.put("SZA", "7004");
        map.put("SZE", "7002");
        map.put("HLT", "7001");

        map.put("INP", "F800");
        map.put("OUT", "F400");
        map.put("SKI", "F200");
        map.put("SKO", "F100");
        map.put("ION", "F080");
        map.put("IOF", "F040");
    }

    public void calistir() {

        System.out.println("Program.txt dosyasi okundu.");

        for (Komutlar komut : komutListesi) {

            String binaryKodu = "";
            String hexKodu = "";

            if (map.containsKey(komut.islem)) {

                String opcode = map.get(komut.islem);

                if (opcode.length() == 1) {

                    int opcodeInt = Integer.parseInt(opcode);
                    int iBiti = komut.iBiti ? 1 : 0;

                    int hafizaAdrInt = 0;
                    if (komut.hafizaAdresi != null) hafizaAdrInt = Integer.parseInt(komut.hafizaAdresi, 16);

                    int makineKodu = (iBiti << 15) | (opcodeInt << 12) | hafizaAdrInt;

                    binaryKodu = String.format("%16s", Integer.toBinaryString(makineKodu)).replace(' ', '0');
                    hexKodu = String.format("%04X", makineKodu);

                } else {
                    int makineKodu = Integer.parseInt(opcode, 16);
                    binaryKodu = String.format("%16s", Integer.toBinaryString(makineKodu)).replace(' ', '0');
                    hexKodu = String.format("%04X", makineKodu);
                }

            } else {
                int value = Integer.parseInt(komut.islem, 16);
                binaryKodu = String.format("%16s", Integer.toBinaryString(value & 0xFFFF)).replace(' ', '0');
                hexKodu = String.format("%04X", value & 0xFFFF);
            }

            String birlestirme = binaryKodu.substring(0, 4) + " " +
                                     binaryKodu.substring(4, 8) + " " +
                                     binaryKodu.substring(8, 12) + " " +
                                     binaryKodu.substring(12, 16);

            System.out.println(komut.adres + " " + birlestirme + " (" + hexKodu + ")");
        }
    }
}
	
