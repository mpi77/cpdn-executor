package cz.sd2.cpdn.executor.utils;

public class Complex {
	public double wx;
	public double wy;
	public double wampl;
	public double wfi;

	public Complex() {
		wx = 0.0;
		wy = 0.0;
		wampl = 0.0;
		wfi = 0.0;
	}

	public Complex(double lx, double ly) {
		wx = lx;
		wy = ly;
		wampl = Math.sqrt(Math.pow(wx, 2) + Math.pow(wy, 2));
		wfi = Math.PI / 2;
		if (wx != 0.0) {
			wfi = Math.atan(wy / wx);
			if (wx < 0 && wy > 0) {
				wfi = Math.PI - Math.abs(wfi);
			}
			if (wx < 0 && wy < 0) {
				wfi = Math.PI + Math.abs(wfi);
			}
		}
	}

	public void clear() {
		wx = 0.0;
		wy = 0.0;
		wampl = 0.0;
		wfi = 0.0;
	}

	public void set(double lx, double ly) {
		wx = lx;
		wy = ly;
		wampl = Math.sqrt(Math.pow(wx, 2) + Math.pow(wy, 2));
		wfi = Math.PI / 2;
		if (wx != 0.0) {
			wfi = Math.atan(wy / wx);
			if (wx < 0 && wy > 0) {
				wfi = Math.PI - Math.abs(wfi);
			}
			if (wx < 0 && wy < 0) {
				wfi = Math.PI + Math.abs(wfi);
			}
		}
	}

	public void seta(double lampl, double lfi) {
		wampl = lampl;
		wfi = lfi;
		wx = wampl * Math.cos(wfi);// *Math.PI);
		wy = wampl * Math.sin(wfi);// *Math.PI);
		if (Math.abs(wx) < 0.0000000001) {
			wx = 0.0;
		}
		if (Math.abs(wy) < 0.0000000001) {
			wy = 0.0;
		}
	}

	public void add(double ax, double ay) {
		wx += ax;
		wy += ay;
		wampl = Math.sqrt(Math.pow(wx, 2) + Math.pow(wy, 2));
		wfi = Math.PI / 2;
		if (wx != 0.0) {
			wfi = Math.atan(wy / wx);
			if (wx < 0 && wy > 0) {
				wfi = Math.PI - Math.abs(wfi);
			}
			if (wx < 0 && wy < 0) {
				wfi = Math.PI + Math.abs(wfi);
			}
		}
	}

	public void min(double ax, double ay) {
		wx -= ax;
		wy -= ay;
		wampl = Math.sqrt(Math.pow(wx, 2) + Math.pow(wy, 2));
		wfi = Math.PI / 2;
		if (wx != 0.0) {
			wfi = Math.atan(wy / wx);
			if (wx < 0 && wy > 0) {
				wfi = Math.PI - Math.abs(wfi);
			}
			if (wx < 0 && wy < 0) {
				wfi = Math.PI + Math.abs(wfi);
			}
		}
	}

	public void multiply(double ax, double ay) {
		double mx = wx;
		wx = (wx * ax) - (wy * ay);
		wy = (mx * ay) + (wy * ax);
		wampl = Math.sqrt(Math.pow(wx, 2) + Math.pow(wy, 2));
		wfi = Math.PI / 2;
		if (wx != 0.0) {
			wfi = Math.atan(wy / wx);
			if (wx < 0 && wy > 0) {
				wfi = Math.PI - Math.abs(wfi);
			}
			if (wx < 0 && wy < 0) {
				wfi = Math.PI + Math.abs(wfi);
			}
		}
	}

	public void delete(double ax, double ay) {
		double mampl = Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2));
		double mfi = Math.PI / 2;
		if (ax != 0.0) {
			mfi = Math.atan(ay / ax);
			if (ax < 0 && ay > 0) {
				mfi = Math.PI - Math.abs(mfi);
			}
			if (ax < 0 && ay < 0) {
				mfi = Math.PI + Math.abs(mfi);
			}
		} else {
			mfi = -1.0 * mfi;
		}
		if (mampl != 0.0) {
			wampl = wampl / mampl;
		}
		wfi = wfi - mfi;
		wx = wampl * Math.cos(wfi);// *Math.PI);
		wy = wampl * Math.sin(wfi);// *Math.PI);
		if (Math.abs(wx) < 0.0000000001) {
			wx = 0.0;
		}
		if (Math.abs(wy) < 0.0000000001) {
			wy = 0.0;
		}
	}

	public void lomx() {
		if (wampl != 0.0) {
			wampl = 1 / wampl;
		}
		wfi = 0.0 - wfi;
		wx = wampl * Math.cos(wfi);
		wy = wampl * Math.sin(wfi);
		if (Math.abs(wx) < 0.0000000001) {
			wx = 0.0;
		}
		if (Math.abs(wy) < 0.0000000001) {
			wy = 0.0;
		}
	}

	public void wkompl_sdruz() {
		wfi = -1.0 * wfi;
		wx = wampl * Math.cos(wfi);// *Math.PI);
		wy = wampl * Math.sin(wfi);// *Math.PI);
		if (Math.abs(wx) < 0.0000000001) {
			wx = 0.0;
		}
		if (Math.abs(wy) < 0.0000000001) {
			wy = 0.0;
		}
	}
}
