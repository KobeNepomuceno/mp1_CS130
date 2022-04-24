
public class Row {
	private String minterm;
	private String binary;
	private int group;
	private boolean used;
	
	public Row(String minterm, String binary, int group, boolean used) {
		super();
		this.minterm = minterm;
		this.binary = binary;
		this.group = group;
		this.used = used;
	}
	public String getMinterm() {
		return minterm;
	}
	public void setMinterm(String minterm) {
		this.minterm = minterm;
	}
	public String getBinary() {
		return binary;
	}
	public void setBinary(String binary) {
		this.binary = binary;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
}
