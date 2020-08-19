package codeGenerator;

/**
 * Created by mohammad hosein on 6/28/2015.
 */
public class Address {
	private int num;
	private TypeAddress Type;
	private VarType varType;

	public Address(int num, VarType varType, TypeAddress Type) {
		this.num = num;
		this.Type = Type;
		this.varType = varType;
	}

	public Address(int num, VarType varType) {
		this.num = num;
		this.Type = TypeAddress.DIRECT;
		this.varType = varType;
	}

	public String toString() {
		switch (Type) {
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
		return Type;
	}
}
