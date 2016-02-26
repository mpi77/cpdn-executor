package cz.sd2.cpdn.executor.launchers;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.apache.http.ParseException;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import cz.sd2.cpdn.executor.resources.Node;
import cz.sd2.cpdn.executor.resources.Section;
import cz.sd2.cpdn.executor.utils.Connection;
import cz.sd2.cpdn.executor.utils.Complex;
import cz.sd2.cpdn.executor.utils.SectionCrate;
import cz.sd2.cpdn.executor.utils.NodeCrate;

public class MainLauncher {
	public static final String CLIENT_ID = "client";
	public static final String USER_NAME = "user";
	public static final String USER_PASSWORD = "abcd";
	public static final int JAVA_IMPORTER_PROFILE = 2;

	public static void main(String[] args) {
		String schema = "";
		int what = 0;

		for (int i = 0; i < args.length; i++) {
			if (args[i].contains("/s:")) {
				schema = args[i].substring(3);
			}
			if (args[i].contains("/w:")) {
				what = Integer.parseInt(args[i].substring(3));
			}
		}
		System.out.println("Schema: " + schema);
		System.out.printf("Program: %d", what);
		System.out.println();
		go(what, schema);
	}

	public static NodeCrate[] readNodes(List<Node> nodes) {
		int num_nodes = nodes.size();
		NodeCrate[] lnodes = new NodeCrate[num_nodes];
		String txtdbl;
		if (num_nodes > 0) {
			for (int loop = 0; loop < num_nodes; loop++) {
				lnodes[loop] = new NodeCrate();
				Node ja = nodes.get(loop);
				lnodes[loop].wid = ja.getId();
				Map<String, Object> node_spec = ja.getSpec();
				switch (node_spec.get("type").toString()) {
				case Node.TYPE_POWER:
					lnodes[loop].wtype = "N";
					break;
				case Node.TYPE_CONSUMPTION:
					lnodes[loop].wtype = "O";
					break;
				case Node.TYPE_TURBO_GEN:
					lnodes[loop].wtype = "G";
					break;
				case Node.TYPE_HYDRO_GEN:
					lnodes[loop].wtype = "H";
					break;
				case Node.TYPE_SUPERIOR_SYSTEM:
					lnodes[loop].wtype = "S";
					break;
				}
				txtdbl = node_spec.get("voltage.level").toString();
				if (!txtdbl.trim().equals("")) {
					lnodes[loop].whladu = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = node_spec.get("voltage.value").toString();
				if (!txtdbl.trim().equals("")) {
					lnodes[loop].wu = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = node_spec.get("voltage.rated").toString();
				if (!txtdbl.trim().equals("")) {
					lnodes[loop].wug = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = node_spec.get("voltage.phase").toString();
				if (!txtdbl.trim().equals("")) {
					lnodes[loop].wfi = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = node_spec.get("power.active").toString();
				if (!txtdbl.trim().equals("")) {
					lnodes[loop].wp = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = node_spec.get("power.reactive").toString();
				if (!txtdbl.trim().equals("")) {
					lnodes[loop].wq = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = node_spec.get("power.rated").toString();
				if (!txtdbl.trim().equals("")) {
					lnodes[loop].ws = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = node_spec.get("cosFi").toString();
				if (!txtdbl.trim().equals("")) {
					lnodes[loop].wsfi = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = node_spec.get("reactance.transverse").toString();
				if (!txtdbl.trim().equals("")) {
					lnodes[loop].wxd = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = node_spec.get("reactance.longitudinal").toString();
				if (!txtdbl.trim().equals("")) {
					lnodes[loop].wxq = Double.parseDouble(txtdbl.trim());
				}
			}
		} else {
		}
		return lnodes;
	}

	public static SectionCrate[] readSections(List<Section> sections) {
		int num_sections = sections.size();
		SectionCrate[] llines = new SectionCrate[num_sections];
		String txtdbl;
		if (num_sections > 0) {
			for (int loop = 0; loop < num_sections; loop++) {
				llines[loop] = new SectionCrate();
				Section ja = sections.get(loop);
				llines[loop].wid = ja.getId();

				Map<String, Integer> sect_nodes = ja.getNodes();
				Map<String, Object> sect_spec = ja.getSpec();

				switch (sect_spec.get("type").toString()) {
				case Section.TYPE_LINE:
					llines[loop].wtype = "V";
					break;
				case Section.TYPE_TRANSFORMER:
					llines[loop].wtype = "T";
					break;
				case Section.TYPE_TRANSFORMER_W3:
					llines[loop].wtype = "U";
					break;
				case Section.TYPE_REACTOR:
					llines[loop].wtype = "R";
					break;
				case Section.TYPE_SWITCH:
					llines[loop].wtype = "S";
					break;
				}
				txtdbl = sect_spec.get("status").toString();
				llines[loop].wstatus = false;
				if (txtdbl.equals(Section.STATUS_ON)) {
					llines[loop].wstatus = true;
				}
				txtdbl = sect_nodes.get("src").toString();
				if (txtdbl != null && !txtdbl.trim().equals("")) {
					llines[loop].widNode1 = sect_nodes.get("src");
				}
				txtdbl = sect_nodes.get("dst").toString();
				if (txtdbl != null && !txtdbl.trim().equals("")) {
					llines[loop].widNode2 = sect_nodes.get("dst");
				}
				if (sect_nodes.get("trc") != null) {
					txtdbl = sect_nodes.get("trc").toString();
					if (txtdbl != null && !txtdbl.trim().equals("")) {
						llines[loop].widNode3 = sect_nodes.get("trc");
					}
				}
				txtdbl = sect_spec.get("susceptance").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wc = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("conductance").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wy = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("current.max").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wimax = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("current.noLoad").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wio = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("reactance.value").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wx = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("resistance.value").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wr = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("losses.noLoad").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wpo = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("losses.short.ab").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wpk1 = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("losses.short.ac").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wpk2 = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("losses.short.bc").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wpk3 = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("power.rated.ab").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].ws1 = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("power.rated.ac").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].ws2 = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("power.rated.bc").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].ws3 = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("voltage.pri.actual").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wup = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("voltage.sec.actual").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wus = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("voltage.trc.actual").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wut = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("voltage.pri.rated").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wunp = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("voltage.sec.rated").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wuns = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("voltage.trc.rated").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wunt = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("voltage.short.ab").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wuk1 = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("voltage.short.ac").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wuk2 = Double.parseDouble(txtdbl.trim());
				}
				txtdbl = sect_spec.get("voltage.short.bc").toString();
				if (!txtdbl.trim().equals("")) {
					llines[loop].wuk3 = Double.parseDouble(txtdbl.trim());
				}
			}
		} else {
		}
		return llines;
	}

	public static int saveNodes(NodeCrate[] nodes) {
		int num_nodes = nodes.length;
		int err = 0;
		try {
			Connection.authenticate(MainLauncher.CLIENT_ID, MainLauncher.USER_NAME, MainLauncher.USER_PASSWORD);
			for (int iw = 0; iw < num_nodes; iw++) {
				Node nod = Node.buildNode(EntityUtils.toString(Node.get(nodes[iw].wid).getEntity()));
				Map<String, Double> node_calc = nod.getCalc();
				node_calc.put("voltage.value", nodes[iw].wvu);
				node_calc.put("voltage.phase", nodes[iw].wvfi);
				nod.setCalc(node_calc);
				JSONObject updateCalc = new JSONObject(EntityUtils.toString(Node.updateCalc(nod).getEntity()));
				int interr = updateCalc.getInt("code");
				if (interr != 200) {
					System.out.println(updateCalc.toString());
					System.out.println(nod);
				}
			}
		} catch (KeyManagementException | JSONException | ParseException | NoSuchAlgorithmException | KeyStoreException
				| IOException | AuthenticationException e) {
			e.printStackTrace();
		}
		return err;
	}

	public static int saveSections(SectionCrate[] sections) {
		int num_sect = sections.length;
		int err = 0;
		try {
			Connection.authenticate(MainLauncher.CLIENT_ID, MainLauncher.USER_NAME, MainLauncher.USER_PASSWORD);
			for (int iw = 0; iw < num_sect; iw++) {
				Section sect = Section.buildSection(EntityUtils.toString(Section.get(sections[iw].wid).getEntity()));
				Map<String, Double> sect_calc = sect.getCalc();
				sect_calc.put("current.src.value", sections[iw].wvi);
				sect_calc.put("current.src.phase", sections[iw].wvfi);
				sect_calc.put("power.src.active", sections[iw].wvp1);
				sect_calc.put("power.src.reactive", sections[iw].wvq1);
				sect_calc.put("power.dst.active", sections[iw].wvp2);
				sect_calc.put("power.dst.reactive", sections[iw].wvq2);
				sect_calc.put("losses.active", sections[iw].wvdq);
				sect_calc.put("losses.reactive", sections[iw].wvdq);
				sect.setCalc(sect_calc);
				JSONObject updateCalc = new JSONObject(EntityUtils.toString(Section.updateCalc(sect).getEntity()));
				int interr = updateCalc.getInt("code");
				if (interr != 200) {
					System.out.println(updateCalc.toString());
					System.out.println(sect);
				}
			}
		} catch (KeyManagementException | JSONException | ParseException | NoSuchAlgorithmException | KeyStoreException
				| IOException | AuthenticationException e) {
			e.printStackTrace();
		}
		return err;
	}

	public static int[][] sortNodes(NodeCrate[] nodes) {
		int num_nodes = nodes.length;
		int[][] aid = new int[2][num_nodes];
		int uka = 0;
		for (int iw = 0; iw < num_nodes; iw++) {
			if (nodes[iw].wtype.equals("N")) {
				aid[0][uka] = iw;
				aid[1][uka] = nodes[iw].wid;
				uka += 1;
			}
		}
		for (int iw = 0; iw < num_nodes; iw++) {
			if (nodes[iw].wtype.equals("G")) {
				aid[0][uka] = iw;
				aid[1][uka] = nodes[iw].wid;
				uka += 1;
			}
		}
		for (int iw = 0; iw < num_nodes; iw++) {
			if (nodes[iw].wtype.equals("O")) {
				aid[0][uka] = iw;
				aid[1][uka] = nodes[iw].wid;
				uka += 1;
			}
		}
		return aid;
	}

	public static Complex[][] admittanceMatrix(int what, int[][] laid, NodeCrate[] nodes, SectionCrate[] sections) {
		int num_nodes = nodes.length;
		int num_sections = sections.length;
		Complex pom = new Complex();
		Complex[][] adm = new Complex[num_nodes][num_nodes];
		for (int iw = 0; iw < num_nodes; iw++) {
			for (int aw = 0; aw < num_nodes; aw++) {
				adm[iw][aw] = new Complex();
			}
		}

		for (int iw = 0; iw < num_nodes; iw++) {
			for (int aw = 0; aw < num_sections; aw++) {
				if (sections[aw].widNode1 == laid[1][iw] || sections[aw].widNode2 == laid[1][iw]) {
					if (sections[aw].widNode1 == laid[1][iw]) {
						for (int uw = iw + 1; uw < num_nodes; uw++) {
							if (sections[aw].widNode2 == laid[1][uw]) {
								switch (sections[aw].wtype) {
								case "S":
									if (sections[aw].wstatus) {
										pom.set(sections[aw].wr, sections[aw].wx);
										pom.lomx();
										pom.multiply(-1, 0);
										adm[iw][uw].set(pom.wx, pom.wy);
										adm[uw][iw].set(pom.wx, pom.wy);
										pom.set(sections[aw].wr, sections[aw].wx);
										pom.lomx();
										adm[iw][iw].add(pom.wx, pom.wy);
										adm[uw][uw].add(pom.wx, pom.wy);
									}
									break;
								case "V":
									pom.set(sections[aw].wr, sections[aw].wx);
									pom.lomx();
									pom.multiply(-1, 0);
									adm[iw][uw].set(pom.wx, pom.wy);
									adm[uw][iw].set(pom.wx, pom.wy);
									pom.set(sections[aw].wr, sections[aw].wx);
									pom.lomx();
									adm[iw][iw].add(pom.wx, pom.wy);
									adm[uw][uw].add(pom.wx, pom.wy);
									pom.set(sections[aw].wy * 0.000001, sections[aw].wc * 0.000001);
									pom.delete(2.0, 0.0);
									adm[iw][iw].add(pom.wx, pom.wy);
									adm[uw][uw].add(pom.wx, pom.wy);
									break;
								case "T":
									Double lt = 0.0;
									Double lr = 0.0;
									Double lx = 0.0;
									Double lg = 0.0;
									Double lc = 0.0;
									boolean oktr = true;
									if (nodes[laid[0][iw]].whladu == sections[aw].wunp) {
										lt = sections[aw].wup / sections[aw].wus;
										lx = (Math.pow(sections[aw].wus, 2) / sections[aw].ws1) * sections[aw].wuk1
												* 0.01;
										if (sections[aw].wus != 0.0) {
											lr = Math.pow(sections[aw].ws1 / sections[aw].wus, 2) * sections[aw].wpk1;
											lc = (sections[aw].ws1 / Math.pow(sections[aw].wus, 2)) * sections[aw].wio
													* 0.01;
											lg = (sections[aw].wpo / Math.pow(sections[aw].wus, 2));
											lc = Math.sqrt(Math.pow(lc, 2) - Math.pow(lg, 2));
										}
									}
									if (nodes[laid[0][iw]].whladu == sections[aw].wus) {
										lt = sections[aw].wus / sections[aw].wup;
										lx = (Math.pow(sections[aw].wup, 2) / sections[aw].ws1) * sections[aw].wuk1
												* 0.01;
										if (sections[aw].wup != 0.0) {
											lr = Math.pow(sections[aw].ws1, 2 / sections[aw].wup) * sections[aw].wpk1;
											lc = (sections[aw].ws1 / Math.pow(sections[aw].wup, 2)) * sections[aw].wio
													* 0.01;
											lg = (sections[aw].wpo / Math.pow(sections[aw].wup, 2));
											lc = Math.sqrt(Math.pow(lc, 2) - Math.pow(lg, 2));
										}
										oktr = false;
									}
									pom.set(lr, lx);
									pom.lomx();
									pom.delete(lt, 0);
									pom.multiply(-1, 0);
									adm[iw][uw].set(pom.wx, pom.wy);
									adm[uw][iw].set(pom.wx, pom.wy);
									pom.set(lr, lx);
									pom.lomx();
									pom.delete(lt, 0);
									adm[iw][iw].add(pom.wx, pom.wy);
									adm[uw][uw].add(pom.wx, pom.wy);
									pom.set(lr, lx);
									pom.lomx();
									pom.multiply((lt - 1) / lt, 0);
									if (!oktr) {
										adm[uw][uw].add(pom.wx, pom.wy);
									} else {
										adm[iw][iw].add(pom.wx, pom.wy);
									}
									pom.set(lr, lx);
									pom.lomx();
									pom.multiply((1 - lt) / Math.pow(lt, 2), 0);
									if (!oktr) {
										adm[iw][iw].add(pom.wx, pom.wy);
									} else {
										adm[uw][uw].add(pom.wx, pom.wy);
									}
									break;
								}
							}
						}
					}
					if (sections[aw].widNode2 == laid[1][iw]) {
						for (int uw = iw + 1; uw < num_nodes; uw++) {
							if (sections[aw].widNode1 == laid[1][uw]) {
								switch (sections[aw].wtype) {
								case "S":
									if (sections[aw].wstatus) {
										pom.set(sections[aw].wr, sections[aw].wx);
										pom.lomx();
										pom.multiply(-1, 0);
										adm[iw][uw].set(pom.wx, pom.wy);
										adm[uw][iw].set(pom.wx, pom.wy);
										pom.set(sections[aw].wr, sections[aw].wx);
										pom.lomx();
										adm[iw][iw].add(pom.wx, pom.wy);
										adm[uw][uw].add(pom.wx, pom.wy);
									}
									break;
								case "V":
									pom.set(sections[aw].wr, sections[aw].wx);
									pom.lomx();
									pom.multiply(-1, 0);
									adm[iw][uw].set(pom.wx, pom.wy);
									adm[uw][iw].set(pom.wx, pom.wy);
									pom.set(sections[aw].wr, sections[aw].wx);
									pom.lomx();
									adm[iw][iw].add(pom.wx, pom.wy);
									adm[uw][uw].add(pom.wx, pom.wy);

									pom.set(sections[aw].wy * 0.000001, sections[aw].wc * 0.000001);
									pom.delete(2.0, 0.0);
									adm[iw][iw].add(pom.wx, pom.wy);
									adm[uw][uw].add(pom.wx, pom.wy);
									break;
								case "T":
									Double lt = 0.0;
									Double lr = 0.0;
									Double lx = 0.0;
									boolean oktr = true;
									if (nodes[laid[0][iw]].whladu == sections[aw].wunp) {
										lt = sections[aw].wup / sections[aw].wus;
										lx = (Math.pow(sections[aw].wus, 2) / sections[aw].ws1) * sections[aw].wuk1
												* 0.01;
									}
									if (nodes[laid[0][iw]].whladu == sections[aw].wuns) {
										lt = sections[aw].wus / sections[aw].wup;
										lx = (Math.pow(sections[aw].wup, 2) / sections[aw].ws1) * sections[aw].wuk1
												* 0.01;
										oktr = false;
									}
									pom.set(lr, lx);
									pom.lomx();
									pom.delete(lt, 0);
									pom.multiply(-1, 0);
									adm[iw][uw].set(pom.wx, pom.wy);
									adm[uw][iw].set(pom.wx, pom.wy);
									pom.set(lr, lx);
									pom.lomx();
									pom.delete(lt, 0);
									adm[iw][iw].add(pom.wx, pom.wy);
									adm[uw][uw].add(pom.wx, pom.wy);

									pom.set(lr, lx);
									pom.lomx();
									pom.multiply((lt - 1) / lt, 0);
									if (!oktr) {
										adm[iw][iw].add(pom.wx, pom.wy);
									} else {
										adm[uw][uw].add(pom.wx, pom.wy);
									}
									pom.set(lr, lx);
									pom.lomx();
									pom.multiply((1 - lt) / Math.pow(lt, 2), 0);
									if (!oktr) {
										adm[uw][uw].add(pom.wx, pom.wy);
									} else {
										adm[iw][iw].add(pom.wx, pom.wy);
									}
									break;
								}
							}
						}
					}
				}
			}
			switch (nodes[laid[0][iw]].wtype) {
			case "O":
				pom.set(nodes[laid[0][iw]].wp, -1.0 * nodes[laid[0][iw]].wq);
				pom.delete(Math.pow(nodes[laid[0][iw]].whladu, 2), 0.0);
				break;
			case "G":
				pom.set(nodes[laid[0][iw]].ws / Math.pow(nodes[laid[0][iw]].whladu, 2), 0.0);
				pom.delete(0.0, nodes[laid[0][iw]].wxd / 100);
				adm[iw][iw].add(pom.wx, pom.wy);
				break;
			}
		}
		return adm;
	}

	public static boolean isDiagonallyDominant(int num_col, Complex[][] adm) {
		boolean r = true;
		for (int loop = 0; loop < num_col; loop++) {
			Complex pom = new Complex();
			pom.add(0.0, 0.0);
			for (int iw = 0; iw < num_col; iw++) {
				if (loop != iw) {
					pom.add(adm[loop][iw].wx, adm[loop][iw].wy);
				}
			}
			if (adm[loop][loop].wampl < pom.wampl) {
				r = false;
				break;
			}
		}
		return r;
	}

	public static Complex[] loadMatrix(int what, int[][] laid, NodeCrate[] nodes) {
		int num_nodes = nodes.length;
		Complex[] load = new Complex[num_nodes];
		Complex pom = new Complex();
		for (int iw = 0; iw < num_nodes; iw++) {
			load[iw] = new Complex();
			switch (nodes[laid[0][iw]].wtype) {
			case "O":
				pom.set(nodes[laid[0][iw]].whladu, 0.0);
				load[iw].set(nodes[laid[0][iw]].wp, -1.0 * nodes[laid[0][iw]].wq);
				if (what == 1) {
					load[iw].delete(pom.wx, pom.wy);
				}
				load[iw].multiply(-1.0, 0.0);
				break;
			case "G":
				pom.set(nodes[laid[0][iw]].ws / Math.pow(nodes[laid[0][iw]].whladu, 2), 0.0);
				pom.delete(0.0, nodes[laid[0][iw]].wxd / 100);
				pom.multiply(nodes[laid[0][iw]].wug * Math.cos(nodes[laid[0][iw]].wfi * Math.PI),
						nodes[laid[0][iw]].wug * Math.sin(nodes[laid[0][iw]].wfi * Math.PI));
				break;
			}
		}
		return load;
	}

	public static Complex[] voltageMatrix(int[][] laid, NodeCrate[] nodes) {
		int num_nodes = nodes.length;
		Complex[] voltage = new Complex[num_nodes];
		double lq = 0.0;
		for (int iw = 0; iw < num_nodes; iw++) {
			voltage[iw] = new Complex();
			switch (nodes[laid[0][iw]].wtype) {
			case "N":
				lq = Math.acos(nodes[laid[0][iw]].wsfi);
				lq = nodes[laid[0][iw]].wu * Math.sin(lq);
				voltage[iw].set(nodes[laid[0][iw]].wu * nodes[laid[0][iw]].wsfi, lq);
				break;
			case "G":
				lq = nodes[laid[0][iw]].wug * Math.sin(Math.acos(nodes[laid[0][iw]].wfi));
				voltage[iw].set(nodes[laid[0][iw]].wug * nodes[laid[0][iw]].wfi, lq);
				break;
			default:
				voltage[iw].set(nodes[laid[0][iw]].whladu, 0.0);
				break;
			}
		}
		return voltage;
	}

	public static NodeCrate[] calcNodes(int[][] laid, NodeCrate[] nodes, Complex[] voltage) {
		int num_nodes = nodes.length;
		for (int iw = 0; iw < num_nodes; iw++) {
			nodes[laid[0][iw]].wvu = voltage[iw].wampl;
			nodes[laid[0][iw]].wvfi = voltage[iw].wfi;
		}
		return nodes;
	}

	public static SectionCrate[] calcSections(NodeCrate[] nodes, SectionCrate[] sections) {
		int num_sect = sections.length;
		int num_nodes = nodes.length;
		for (int loop = 0; loop < num_sect; loop++) {
			for (int iw = 0; iw < num_nodes; iw++) {
				if (sections[loop].widNode1 == nodes[iw].wid) {
					sections[loop].wvu1 = nodes[iw].wvu;
					sections[loop].wvfu1 = nodes[iw].wvfi;
				}
				if (sections[loop].widNode2 == nodes[iw].wid) {
					sections[loop].wvu2 = nodes[iw].wvu;
					sections[loop].wvfu2 = nodes[iw].wvfi;
				}
				if (sections[loop].widNode3 == nodes[iw].wid) {
					sections[loop].wvu3 = nodes[iw].wvu;
					sections[loop].wvfu3 = nodes[iw].wvfi;
				}
			}
		}
		for (int loop = 0; loop < num_sect; loop++) {
			if (sections[loop].wtype == "V") {
				Complex pom = new Complex(sections[loop].wvu1 * Math.cos(sections[loop].wvfu1),
						sections[loop].wvu1 * Math.sin(sections[loop].wvfu1));
				pom.delete(sections[loop].wr, sections[loop].wx);
				sections[loop].wvi = pom.wampl;
				sections[loop].wvfi = pom.wfi;
				Complex poma = new Complex();
				poma.set(sections[loop].wvu1 * Math.cos(sections[loop].wvfu1),
						sections[loop].wvu1 * Math.sin(sections[loop].wvfu1));
				poma.multiply(pom.wx, pom.wy);
				sections[loop].wvp1 = poma.wx;
				sections[loop].wvq1 = poma.wy;
				poma.set(sections[loop].wvu2 * Math.cos(sections[loop].wvfu2),
						sections[loop].wvu2 * Math.sin(sections[loop].wvfu2));
				poma.multiply(pom.wx, pom.wy);
				sections[loop].wvp2 = poma.wx;
				sections[loop].wvq2 = poma.wy;
				pom.set(sections[loop].wvp1, sections[loop].wvq1);
				pom.min(poma.wx, poma.wy);
				sections[loop].wvdp = pom.wx;
				sections[loop].wvdq = pom.wy;
			}
		}
		return sections;
	}

	public static boolean[] voltageTest(int[][] laid, NodeCrate[] nodes) {
		int num_nodes = nodes.length;
		boolean[] lok = new boolean[num_nodes];
		for (int iw = 0; iw < num_nodes; iw++) {
			switch (nodes[laid[0][iw]].wtype) {
			case "N":
				lok[iw] = false;
				break;
			case "G":
				lok[iw] = false;
				break;
			default:
				lok[iw] = true;
				break;
			}
		}
		return lok;
	}

	public static Double[][] jacobi(Complex[][] adm, Complex[] voltage, Complex[] load) {
		int num_nodes = voltage.length;
		Double[][] jac = new Double[num_nodes * 2 - 2][num_nodes * 2 - 2];
		Double pom1 = 0.0;
		Double pom2 = 0.0;
		Double pom3 = 0.0;
		Double pom4 = 0.0;
		for (int loop = 1; loop < num_nodes; loop++) {
			pom1 = 0.0;
			pom2 = 0.0;
			pom3 = 0.0;
			pom4 = 0.0;
			for (int iw = 1; iw < num_nodes; iw++) {
				if (iw != loop) {
					double uhel = voltage[loop].wfi - voltage[iw].wfi - adm[loop][iw].wfi;
					jac[loop - 1][iw - 1] = adm[loop][iw].wampl * voltage[loop].wampl * voltage[iw].wampl
							* Math.cos(uhel);
					jac[loop - 1][num_nodes + iw - 2] = adm[loop][iw].wampl * voltage[loop].wampl * Math.cos(uhel);

					jac[num_nodes + loop - 2][iw - 1] = -1.0 * adm[loop][iw].wampl * voltage[loop].wampl
							* voltage[iw].wampl * Math.cos(uhel);
					jac[num_nodes + loop - 2][num_nodes + iw - 2] = adm[loop][iw].wampl * voltage[loop].wampl
							* Math.sin(uhel);

					pom1 += adm[loop][iw].wampl * voltage[loop].wampl * voltage[iw].wampl * Math.sin(-1.0 * uhel);
					pom2 += adm[loop][iw].wampl * voltage[iw].wampl * Math.cos(uhel);
					pom3 += adm[loop][iw].wampl * voltage[loop].wampl * voltage[iw].wampl * Math.cos(uhel);
					pom4 += adm[loop][iw].wampl * voltage[iw].wampl * Math.sin(uhel);// OK
				}
			}
			jac[loop - 1][loop - 1] = pom1;
			jac[loop - 1][num_nodes + loop - 2] = 2.0 * adm[loop][loop].wampl * voltage[loop].wampl
					* Math.cos(adm[loop][loop].wfi) + pom2;
			jac[num_nodes + loop - 2][loop - 1] = pom3;
			jac[num_nodes + loop - 2][num_nodes + loop - 2] = -2.0 * adm[loop][loop].wampl * voltage[loop].wampl
					* Math.sin(adm[loop][loop].wfi) + pom4;
		}
		return jac;
	}

	public static Double[] jacobiUfi(Complex[] voltage) {
		int num_nodes = voltage.length;
		Double[] jac_ufi = new Double[num_nodes * 2 - 2];

		for (int loop = 1; loop < num_nodes; loop++) {
			jac_ufi[loop - 1] = voltage[loop].wampl;
			jac_ufi[num_nodes + loop - 2] = voltage[loop].wfi;
		}
		return jac_ufi;
	}

	public static Double[] jacobiPq(Complex[] load) {
		int num_nodes = load.length;
		Double[] jac_pq = new Double[num_nodes * 2 - 2];

		for (int loop = 1; loop < num_nodes; loop++) {
			jac_pq[loop - 1] = load[loop].wx;
			jac_pq[num_nodes + loop - 2] = load[loop].wy;
		}
		return jac_pq;
	}

	public static Complex[] newtonRaphson(Complex[][] adm, Complex[] voltage, Complex[] load, Double[] jac_pq,
			double expr, int lnumiter) {
		int num_mat = voltage.length - 1;
		Double[] jac_res = new Double[num_mat * 2];
		Double[] ljac_pq = new Double[num_mat * 2];
		Double diff = 0.0;
		int numiter = 0;
		Double[][] jacobi;
		Double[] jacob_dpq = new Double[num_mat * 2];
		Double[] jacob_rufi = new Double[num_mat * 2];
		Double[] jacob_dufi = new Double[num_mat * 2];
		Complex pom = new Complex();
		Complex pon = new Complex();
		for (int loop = 0; loop < num_mat; loop++) {
			jacob_dufi[loop] = -1.0;
			jacob_dufi[num_mat + loop] = -1.0;
			ljac_pq[loop] = jac_pq[loop];
			ljac_pq[num_mat + loop] = jac_pq[num_mat + loop];
		}
		do {
			for (int loop = 0; loop < num_mat; loop++) {
				jac_res[loop] = 0.0;
				jac_res[num_mat + loop] = 0.0;
				jacob_dpq[loop] = 0.0;
				jacob_dpq[num_mat + loop] = 0.0;

				for (int iw = 0; iw < num_mat + 1; iw++) {
					pom.set(voltage[iw].wx, voltage[iw].wy);
					pom.multiply(adm[loop + 1][iw].wx, adm[loop + 1][iw].wy);
					pon.set(voltage[loop + 1].wx, voltage[loop + 1].wy);
					pon.wkompl_sdruz();
					pom.multiply(pon.wx, pon.wy);
					jac_res[loop] += pom.wx;
					jac_res[num_mat + loop] += pom.wy;
				}
				jacob_dpq[loop] = ljac_pq[loop] - jac_res[loop];
				jacob_dpq[num_mat + loop] = ljac_pq[num_mat + loop] - jac_res[num_mat + loop];
				ljac_pq[loop] = jac_res[loop];
				ljac_pq[num_mat + loop] = jac_res[num_mat + loop];
				if (jacob_dpq[loop] > 0.0) {
					jacob_dufi[loop] = -1.0 * jacob_dufi[loop];
				}
				if (jacob_dpq[num_mat + loop] > 0.0) {
					jacob_dufi[num_mat + loop] = -1.0 * jacob_dufi[num_mat + loop];
				}

				if (Math.abs(jacob_dpq[loop]) > diff) {
					diff = Math.abs(jacob_dpq[loop]);
				}
				if (Math.abs(jacob_dpq[num_mat + loop]) > diff) {
					diff = Math.abs(jacob_dpq[num_mat + loop]);
				}
			}
			System.out.println("");
			System.out.print(String.valueOf(numiter).concat("..."));
			for (int iw = 0; iw < num_mat * 2; iw++) {
				System.out.print(String.valueOf(jacob_dpq[iw]).concat(";"));
			}
			System.out.println("");
			if (diff > expr) {
				jacobi = jacobi(adm, voltage, load);

				for (int loop = 0; loop < num_mat * 2; loop++) {
					jacob_rufi[loop] = 0.0;
					for (int iw = 0; iw < num_mat * 2; iw++) {
						if (iw != loop) {
							jacob_rufi[loop] += jacobi[loop][iw] * jacob_dufi[iw];
						}
					}
					jacob_rufi[loop] = (jacob_dpq[loop] - jacob_rufi[loop]) / jacobi[loop][loop];
				}
				System.out.println("");
				for (int loop = 0; loop < num_mat; loop++) {
					for (int iw = 0; iw < num_mat * 2; iw++) {
						System.out.print(String.valueOf(jacobi[loop][iw].toString().concat(";")));
					}
					System.out.print("....");
					System.out.print(String.valueOf(jacob_dufi[loop]));
					System.out.print("....");
					System.out.print(String.valueOf(jacob_rufi[loop]));
					System.out.print("....");
					System.out.print(String.valueOf(jacob_dpq[loop]));
					System.out.println("");
				}
				for (int loop = 0; loop < num_mat; loop++) {
					for (int iw = 0; iw < num_mat * 2; iw++) {
						System.out.print(String.valueOf(jacobi[loop + num_mat][iw].toString().concat(";")));
					}
					System.out.print("....");
					System.out.print(String.valueOf(jacob_dufi[loop + num_mat]));
					System.out.print("....");
					System.out.print(String.valueOf(jacob_rufi[loop + num_mat]));
					System.out.print("....");
					System.out.print(String.valueOf(jacob_dpq[loop + num_mat]));
					System.out.println("");
				}

				for (int loop = 0; loop < num_mat; loop++) {
					jacob_dufi[loop] = jacob_rufi[loop];
					jacob_dufi[num_mat + loop] = jacob_rufi[num_mat + loop];
					voltage[loop + 1].set(
							voltage[loop + 1].wx + jacob_rufi[num_mat + loop] * Math.cos(jacob_rufi[loop]),
							voltage[loop + 1].wy + jacob_rufi[num_mat + loop] * Math.sin(jacob_rufi[loop]));
				}
			}
			numiter += 1;
			System.out.println("");
			System.out.print(String.valueOf(numiter).concat("..."));
			for (int iw = 0; iw < num_mat + 1; iw++) {
				System.out.print(String.valueOf(voltage[iw].wx).concat("j"));
				System.out.print(String.valueOf(voltage[iw].wy).concat(";"));
			}
			System.out.println("");
		} while (numiter < lnumiter && diff > expr);

		return voltage;
	}

	public static Complex[] gaussSeidel(int[][] laid, Complex[][] adm, Complex[] voltage, Complex[] load,
			boolean[] okpoc, double expr, int lnumiter) {
		int pocu = voltage.length;
		Complex[] res = new Complex[pocu];
		for (int iw = 0; iw < pocu; iw++) {
			res[iw] = new Complex(voltage[iw].wx, voltage[iw].wy);
		}
		Complex s1 = new Complex();
		Complex s2 = new Complex();
		Complex pom = new Complex();
		Complex b = new Complex();
		int numiter = 0;
		double diff = 0.0;
		do {
			for (int loop = 0; loop < pocu; loop++) {
				if (okpoc[loop]) {
					s1.set(0.0, 0.0);
					s2.set(0.0, 0.0);
					diff = 0.0;
					for (int j = 0; j < loop; j++) {
						pom.set(adm[loop][j].wx, adm[loop][j].wy);
						pom.multiply(voltage[j].wx, voltage[j].wy);
						s1.add(pom.wx, pom.wy);
					}
					for (int k = loop + 1; k < pocu; k++) {
						pom.set(adm[loop][k].wx, adm[loop][k].wy);
						pom.multiply(voltage[k].wx, voltage[k].wy);
						s2.add(pom.wx, pom.wy);
					}
					pom.set(1.0, 0.0);
					pom.delete(adm[loop][loop].wx, adm[loop][loop].wy);
					b.set(load[loop].wx, load[loop].wy);
					b.min(s1.wx, s1.wy);
					b.min(s2.wx, s2.wy);
					pom.multiply(b.wx, b.wy);
					res[loop].set(pom.wx, pom.wy);
					pom.set(voltage[loop].wx, voltage[loop].wy);
					pom.min(res[loop].wx, res[loop].wy);
					if (pom.wampl > diff) {
						diff = pom.wampl;
					}
					voltage[loop].set(res[loop].wx, res[loop].wy);
				}
			}
			System.out.print(String.valueOf(numiter).concat("..."));
			System.out.print(String.valueOf(diff).concat("..."));
			System.out.println("");
			numiter += 1;
		} while (numiter < lnumiter && diff > expr);
		return res;
	}

	public static int go(int what, String scheme) {
		int err = 0;
		if (scheme != "") {
			int schemeId = Integer.parseInt(scheme);
			List<Node> nodes;
			List<Section> sections;
			NodeCrate[] wnodes;
			SectionCrate[] wsections;
			try {
				Connection.authenticate(MainLauncher.CLIENT_ID, MainLauncher.USER_NAME, MainLauncher.USER_PASSWORD);

				nodes = Node.buildNodes(EntityUtils.toString(Node.getAll(schemeId, 1000).getEntity()));
				sections = Section.buildSections(EntityUtils.toString(Section.getAll(schemeId, 1000).getEntity()));

				wnodes = readNodes(nodes);
				wsections = readSections(sections);
			} catch (KeyManagementException | JSONException | ParseException | NoSuchAlgorithmException
					| KeyStoreException | IOException | AuthenticationException e) {
				e.printStackTrace();
				err = 1;
				return err;
			}

			int[][] aid = sortNodes(wnodes);
			Complex[][] adm = admittanceMatrix(what, aid, wnodes, wsections);
			Complex[] load = loadMatrix(what, aid, wnodes);
			Complex[] voltage = voltageMatrix(aid, wnodes);
			boolean[] okVoltage = voltageTest(aid, wnodes);
			int num_nodes = wnodes.length;
			for (int iw = 0; iw < num_nodes; iw++) {
				System.out.print(String.valueOf(voltage[iw].wx).concat("j"));
				System.out.print(String.valueOf(voltage[iw].wy).concat(";"));
				System.out.print("...");
				for (int uw = 0; uw < wnodes.length; uw++) {
					System.out.print(String.valueOf(adm[iw][uw].wx).concat("j"));
					System.out.print(String.valueOf(adm[iw][uw].wy).concat(";"));
				}
				System.out.print("...");
				System.out.print(String.valueOf(load[iw].wx).concat("j"));
				System.out.println(String.valueOf(load[iw].wy).concat(";"));
			}
			Complex[] naq = new Complex[num_nodes];
			boolean okCalc = isDiagonallyDominant(num_nodes, adm);
			if (okCalc) {
				switch (what) {
				case 1:
					naq = gaussSeidel(aid, adm, voltage, load, okVoltage, 0.000001, 1000);
					break;
				case 2:
					Double[] jac_pq = jacobiPq(load);
					naq = newtonRaphson(adm, voltage, load, jac_pq, 0.05, 10);
					break;
				}
			} else {
				err = 2;
				return err;
			}
			wnodes = calcNodes(aid, wnodes, naq);
			saveNodes(wnodes);
			wsections = calcSections(wnodes, wsections);
			saveSections(wsections);
		}
		switch (err) {
		case 0:
			System.out.println("OK");
			break;
		case 1:
			System.out.println("Error while fetching data from app server.");
			break;
		case 2:
			System.out.println("Admittance matrix is not diagonally dominant.");
			break;
		}
		return err;
	}

}
