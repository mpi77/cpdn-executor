package cz.sd2.cpdn.executor.utils;

public class SectionCrate {
	public int wid;
	public int widNode1;
	public int widNode2;
	public int widNode3;
	public String wtype;
	public boolean wstatus;
	public double wr;
	public double wx;
	public double wc;
	public double wy;
	public double ws1;
	public double ws2;
	public double ws3;
	public double wuk1;
	public double wuk2;
	public double wuk3;
	public double wpk1;
	public double wpk2;
	public double wpk3;
	public double wio;
	public double wpo;
	public double wup;
	public double wus;
	public double wut;
	public double wunp;
	public double wuns;
	public double wunt;
	public double wimax;
	public double wvu1;
	public double wvu2;
	public double wvu3;
	public double wvfu1;
	public double wvfu2;
	public double wvfu3;
	public double wvi;
	public double wvfi;
	public double wvp1;
	public double wvq1;
	public double wvp2;
	public double wvq2;
	public double wvdp;
	public double wvdq;

	public SectionCrate() {
		wid = 0;
		widNode1 = 0;
		widNode2 = 0;
		widNode3 = 0;
		wr = 0.0;
		wx = 0.0;
		wc = 0.0;
		wy = 0.0;
		wtype = "";
		ws1 = 0.0;
		ws2 = 0.0;
		ws3 = 0.0;
		wuk1 = 0.0;
		wuk2 = 0.0;
		wuk3 = 0.0;
		wimax = 0.0;
		wpo = 0.0;
		wup = 0.0;
		wus = 0.0;
		wut = 0.0;
		wunp = 0.0;
		wuns = 0.0;
		wunt = 0.0;
		wstatus = true;
		wio = 0.0;
		wpk1 = 0.0;
		wpk2 = 0.0;
		wpk3 = 0.0;
		wvi = 0.0;
		wvfi = 0.0;
		wvp1 = 0.0;
		wvq1 = 0.0;
		wvp2 = 0.0;
		wvq2 = 0.0;
		wvu1 = 0.0;
		wvfu1 = 0.0;
		wvu2 = 0.0;
		wvfu2 = 0.0;
		wvu3 = 0.0;
		wvfu3 = 0.0;
	}

	public SectionCrate(int lid, int lNode1, int lNode2, int lNode3, String ltype, double lr, double lx, double lc,
			double ly, double ls1, double ls2, double ls3, double luk1, double luk2, double luk3, double lpk1,
			double lpk2, double lpk3, double lup, double lus, double lut, double lunp, double luns, double lunt,
			double lio, double limax, boolean lstatus) {
		wid = lid;
		widNode1 = lNode1;
		widNode2 = lNode2;
		widNode3 = lNode3;
		wtype = ltype;
		wr = lr;
		wx = lx;
		wc = lc;
		wy = ly;
		ws1 = ls1;
		ws2 = ls2;
		ws3 = ls3;
		wuk1 = luk1;
		wuk2 = luk2;
		wuk3 = luk3;
		wio = lio;
		wpk1 = lpk1;
		wpk2 = lpk2;
		wpk3 = lpk3;
		wup = lup;
		wus = lus;
		wut = lut;
		wunp = lunp;
		wuns = luns;
		wunt = lunt;
		wstatus = lstatus;
		wimax = limax;
		wvi = 0.0;
		wvfi = 0.0;
		wvp1 = 0.0;
		wvq1 = 0.0;
		wvp2 = 0.0;
		wvq2 = 0.0;
		wvu1 = 0.0;
		wvfu1 = 0.0;
		wvu2 = 0.0;
		wvfu2 = 0.0;
		wvu3 = 0.0;
		wvfu3 = 0.0;
	}
}
