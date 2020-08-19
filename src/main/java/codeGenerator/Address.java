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
		this.Type = TypeAddress.Direct;
		this.varType = varType;
	}

	public String toString() {
		switch (Type) {
			case Direct: //I know that it's redundant but we keep it for readability
				return num + "";
			case Indirect:
				return "@" + num;
			case Immediate:
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
