package model;

public class SheetLine {
	private String number;
	private String type;
	private String name;
	private String address;
	private int unp;
	private long okpo;
	private long account;
	private boolean isNets;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getUnp() {
		return unp;
	}

	public void setUnp(int unp) {
		this.unp = unp;
	}

	public long getOkpo() {
		return okpo;
	}

	public void setOkpo(long okpo) {
		this.okpo = okpo;
	}

	public long getAccount() {
		return account;
	}

	public void setAccount(long account) {
		this.account = account;
	}

	public boolean isNets() {
		return isNets;
	}

	public void setNets(boolean isNets) {
		this.isNets = isNets;
	}
}
