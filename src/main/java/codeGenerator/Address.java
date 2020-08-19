package codeGenerator;

/**
 * Created by mohammad hosein on 6/28/2015.
 */
public class Address {
	private int num;
	private TypeAddress type;
	private VarType varType;

	public Address(int num, VarType varType, TypeAddress type) {
		this.setNum(num);
		this.setType(type);
		this.setVarType(varType);
	}

	public Address(int num, VarType varType) {
		this.setNum(num);
		this.setType(TypeAddress.Direct);
		this.setVarType(varType);
	}

	public String toString() {
		switch (this.getType()) {
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
		return type;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setType(TypeAddress type) {
		this.type = type;
	}

	public void setVarType(VarType varType) {
		this.varType = varType;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public void setType(TypeAddress type) {
		this.type = type;
	}

	public void setVarType(VarType varType) {
		this.varType = varType;
	}
}
