package org.worlditplanet.java;

/**
 * Created by gamezovladislav on 02.06.2017.
 */
public class Tariff {
    private long id;
    private String name;
    private double abonentFee;
    private double smsPrice;
    private double callPrice;
    private double intPrice;
    private int smsPack;
    private int callPack;
    private long intPack;

    public Tariff(
            long id, String name,
            double abonentFee,
            double smsPrice, double callPrice, double intPrice,
            int smsPack, int callPack, long intPack) {
        this.id = id;
        this.name = name;
        this.abonentFee = abonentFee;
        this.smsPrice = smsPrice;
        this.callPrice = callPrice;
        this.intPrice = intPrice;
        this.smsPack = smsPack;
        this.callPack = callPack;
        this.intPack = intPack;
    }

    @Override
    public String toString() {
        return "Tariff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abonentFee=" + abonentFee +
                ", smsPrice=" + smsPrice +
                ", callPrice=" + callPrice +
                ", intPrice=" + intPrice +
                ", smsPack=" + smsPack +
                ", callPack=" + callPack +
                ", intPack=" + intPack +
                '}';
    }
}
