/** 
 * (c) 2018 Nélson Rafael Martins All Rights Reserverd
 **/

package reader;

public class CompareLineCode {
	private Line[] linesToDisplay;
	private Line[] linesToDisplayTotal;

	public CompareLineCode(CSVReader reader) {
		linesToDisplay = new Line[Uploader.lineCodes.length];
		linesToDisplayTotal = new Line[Uploader.lineCodes.length];
		for (int i = 0; i < Uploader.lineCodes.length; i++) {
			for (int j = 0; j < reader.getLineList().size(); j++) {
				if (Uploader.lineCodes[i] == reader.getLineList().get(j).get_cTrab()
						&& !reader.getLineList().get(j).get_turno().equals("TOTAL")) {
					linesToDisplay[i] = reader.getLineList().get(j);
				} else if (Uploader.lineCodes[i] == reader.getLineList().get(j).get_cTrab()
						&& reader.getLineList().get(j).get_turno().equals("TOTAL")) {
					linesToDisplayTotal[i] = reader.getLineList().get(j);
				}
			}
		}
		for (int i = 0; i < Uploader.lineCodes.length; i++) {
			//System.out.println(linesToDisplayTotal[i].get_cTrab() + " ---- " + linesToDisplay[i].get_cTrab());
			linesToDisplayTotal[i].set_descritivo(linesToDisplay[i].get_descritivo());
			linesToDisplayTotal[i].set_status(linesToDisplay[i].get_status());
		}
	}

	public Line[] getLinesToDisplay() {
		return this.linesToDisplay;
	}

	public Line[] getLinesToDisplayTotal() {
		return this.linesToDisplayTotal;
	}
}
