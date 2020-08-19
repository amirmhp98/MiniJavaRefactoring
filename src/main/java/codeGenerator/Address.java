package codeGenerator;

/**
 * Created by mohammad hosein on 6/28/2015.
 */
public class Address {
	private int num;
	private TypeAddress type;
	private VarType varType;

	public Address(int num, VarType varType, TypeAddress type) {
		this.num = num;
		this.type = type;
		this.varType = varType;
	}

	public Address(int num, VarType varType) {
		this.num = num;
		this.type = TypeAddress.DIRECT;
		this.varType = varType;
	}

	public String toString() {
		switch (type) {
			case DIRECT: //I know that it's redundant but we keep it for readability
				return num + "";
			case INDIRECT:
				return "@" + num;
			case IMMEDIATE:
				return "#" + num;
			default:
				return num + "";
		}
	}

	public VarType getVarType() {
		return varType;
	}

	public int getNum() {
		return num;
	}

	public TypeAddress getType() {
		return type;
	}
}
