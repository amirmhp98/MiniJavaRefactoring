package codeGenerator;

import log.Log;
import errorHandler.ErrorHandler;
import scanner.token.Token;
import semantic.symbol.Symbol;
import semantic.symbol.SymbolTable;
import semantic.symbol.SymbolType;

import java.util.Stack;

/**
 * Created by Alireza on 6/27/2015.
 */
public class CodeGenerator {
	private Memory memory = new Memory();
	private Stack<Address> ss = new Stack<Address>();
	private Stack<String> symbolStack = new Stack<>();
	private Stack<String> callStack = new Stack<>();
	private SymbolTable symbolTable;

	public CodeGenerator() {
		symbolTable = new SymbolTable(memory);
		//TODO
	}

	public void printMemory() {
		memory.printCodeBlock();
	}

	public void semanticFunction(int func, Token next) {
		Log.print("codegenerator : " + func);
		switch (func) {
			case 0:
				return;
			case 1:
				checkID();
				break;
			case 2:
				pid(next);
				break;
			case 3:
				fPid();
				break;
			case 4:
				kpid(next);
				break;
			case 5:
				intpid(next);
				break;
			case 6:
				startCall();
				break;
			case 7:
				call();
				break;
			case 8:
				arg();
				break;
			case 9:
				assign();
				break;
			case 10:
				add();
				break;
			case 11:
				sub();
				break;
			case 12:
				mult();
				break;
			case 13:
				label();
				break;
			case 14:
				save();
				break;
			case 15:
				_while();
				break;
			case 16:
				jpf_save();
				break;
			case 17:
				jpHere();
				break;
			case 18:
				print();
				break;
			case 19:
				equal();
				break;
			case 20:
				less_than();
				break;
			case 21:
				and();
				break;
			case 22:
				not();
				break;
			case 23:
				defClass();
				break;
			case 24:
				defMethod();
				break;
			case 25:
				popClass();
				break;
			case 26:
				extend();
				break;
			case 27:
				defField();
				break;
			case 28:
				defVar();
				break;
			case 29:
				methodReturn();
				break;
			case 30:
				defParam();
				break;
			case 31:
				lastTypeBool();
				break;
			case 32:
				lastTypeInt();
				break;
			case 33:
				defMain();
				break;
		}
	}

	private void defMain() {
		//ss.pop();
		memory.add3AddressCode(ss.pop().getNum(), Operation.JP, new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS), null, null);
		String methodName = "main";
		String className = symbolStack.pop();

		symbolTable.addMethod(className, methodName, memory.getCurrentCodeBlockAddress());

		symbolStack.push(className);
		symbolStack.push(methodName);
	}

	//    public void spid(Token next){
//        symbolStack.push(next.getValue());
//    }
	public void checkID() {
		symbolStack.pop();
		if (ss.peek().getVarType() == VarType.NON) {
			//TODO : error
		}
	}

	public void pid(Token next) {
		if (symbolStack.size() > 1) {
			String methodName = symbolStack.pop();
			String className = symbolStack.pop();
			try {

				Symbol s = symbolTable.get(className, methodName, next.getValue());
				VarType t = extractVarType(s.getType());
				ss.push(new Address(s.getAddress(), t));


			} catch (Exception e) {
				ss.push(new Address(0, VarType.NON));
			}
			symbolStack.push(className);
			symbolStack.push(methodName);
		} else {
			ss.push(new Address(0, VarType.NON));
		}
		symbolStack.push(next.getValue());
	}


	public void fPid() {
		ss.pop();
		ss.pop();

		Symbol s = symbolTable.get(symbolStack.pop(), symbolStack.pop());
		VarType t = extractVarType(s.getType());
		ss.push(new Address(s.getAddress(), t));

	}

	public void kpid(Token next) {
		ss.push(symbolTable.get(next.getValue()));
	}

	public void intpid(Token next) {
		ss.push(new Address(Integer.parseInt(next.getValue()), VarType.INT, TypeAddress.IMMEDIATE));
	}

	public void startCall() {
		//TODO: method ok
		ss.pop();
		ss.pop();
		String methodName = symbolStack.pop();
		String className = symbolStack.pop();
		symbolTable.startCall(className, methodName);
		callStack.push(className);
		callStack.push(methodName);

		//symbolStack.push(methodName);
	}

	public void call() {
		//TODO: method ok
		String methodName = callStack.pop();
		String className = callStack.pop();
		try {
			symbolTable.getNextParam(className, methodName);
			ErrorHandler.printError("The few argument pass for method");
		} catch (IndexOutOfBoundsException e) {
		}
		SymbolType symbolType = symbolTable.getMethodReturnType(className, methodName);
		VarType t = extractVarType(symbolType);
		Address temp = new Address(memory.getTemp(), t);
		ss.push(temp);
		memory.add3AddressCode(Operation.ASSIGN, new Address(temp.getNum(), VarType.ADDRESS, TypeAddress.IMMEDIATE), new Address(symbolTable.getMethodReturnAddress(className, methodName), VarType.ADDRESS), null);
		memory.add3AddressCode(Operation.ASSIGN, new Address(memory.getCurrentCodeBlockAddress() + 2, VarType.ADDRESS, TypeAddress.IMMEDIATE), new Address(symbolTable.getMethodCallerAddress(className, methodName), VarType.ADDRESS), null);
		memory.add3AddressCode(Operation.JP, new Address(symbolTable.getMethodAddress(className, methodName), VarType.ADDRESS), null, null);

		//symbolStack.pop();


	}

	public void arg() {
		//TODO: method ok

		String methodName = callStack.pop();
//        String className = symbolStack.pop();
		try {
			Symbol s = symbolTable.getNextParam(callStack.peek(), methodName);
			VarType t = extractVarType(s.getType());
			Address param = ss.pop();
			if (param.getVarType() != t) {
				ErrorHandler.printError("The argument type isn't match");
			}
			memory.add3AddressCode(Operation.ASSIGN, param, new Address(s.getAddress(), t), null);

//        symbolStack.push(className);

		} catch (IndexOutOfBoundsException e) {
			ErrorHandler.printError("Too many arguments pass for method");
		}
		callStack.push(methodName);

	}

	public void assign() {

		Address s1 = ss.pop();
		Address s2 = ss.pop();
//        try {
		checkSameOperandForOperation(s1, s2, "assign");
//        }catch (NullPointerException d)
//        {
//            d.printStackTrace();
//        }
		memory.add3AddressCode(Operation.ASSIGN, s1, s2, null);

	}

	public void add() {
		Address temp = new Address(memory.getTemp(), VarType.INT);
		Address s2 = ss.pop();
		Address s1 = ss.pop();

		checkIntOperandsForOperation(s2, s1, "add");

		memory.add3AddressCode(Operation.ADD, s1, s2, temp);
		ss.push(temp);
	}


	public void sub() {
		Address temp = new Address(memory.getTemp(), VarType.INT);
		Address s2 = ss.pop();
		Address s1 = ss.pop();

		checkIntOperandsForOperation(s2, s1, "sub");

		memory.add3AddressCode(Operation.SUB, s1, s2, temp);
		ss.push(temp);
	}

	public void mult() {
		Address temp = new Address(memory.getTemp(), VarType.INT);
		Address s2 = ss.pop();
		Address s1 = ss.pop();

		checkIntOperandsForOperation(s2, s1, "mult");

		memory.add3AddressCode(Operation.MULT, s1, s2, temp);
//        memory.saveMemory();
		ss.push(temp);
	}

	public void label() {
		ss.push(new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS));
	}

	public void save() {
		ss.push(new Address(memory.saveMemory(), VarType.ADDRESS));
	}

	public void _while() {
		memory.add3AddressCode(ss.pop().getNum(), Operation.JPF, ss.pop(), new Address(memory.getCurrentCodeBlockAddress() + 1, VarType.ADDRESS), null);
		memory.add3AddressCode(Operation.JP, ss.pop(), null, null);
	}

	public void jpf_save() {
		Address save = new Address(memory.saveMemory(), VarType.ADDRESS);
		memory.add3AddressCode(ss.pop().getNum(), Operation.JPF, ss.pop(), new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS), null);
		ss.push(save);
	}

	public void jpHere() {
		memory.add3AddressCode(ss.pop().getNum(), Operation.JP, new Address(memory.getCurrentCodeBlockAddress(), VarType.ADDRESS), null, null);
	}

	public void print() {
		memory.add3AddressCode(Operation.PRINT, ss.pop(), null, null);
	}

	public void equal() {
		Address temp = new Address(memory.getTemp(), VarType.BOOL);
		Address s2 = ss.pop();
		Address s1 = ss.pop();
		checkSameOperandForOperation(s2, s1, "equal");
		memory.add3AddressCode(Operation.EQ, s1, s2, temp);
		ss.push(temp);
	}


	public void less_than() {
		Address temp = new Address(memory.getTemp(), VarType.BOOL);
		Address s2 = ss.pop();
		Address s1 = ss.pop();
		if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
			ErrorHandler.printError("The type of operands in less than operator is different");
		}
		memory.add3AddressCode(Operation.LT, s1, s2, temp);
		ss.push(temp);
	}

	public void and() {
		Address temp = new Address(memory.getTemp(), VarType.BOOL);
		Address s2 = ss.pop();
		Address s1 = ss.pop();
		if (s1.getVarType() != VarType.BOOL || s2.getVarType() != VarType.BOOL) {
			ErrorHandler.printError("In and operator the operands must be boolean");
		}
		memory.add3AddressCode(Operation.AND, s1, s2, temp);
		ss.push(temp);

	}

	public void not() {
		Address temp = new Address(memory.getTemp(), VarType.BOOL);
		Address s2 = ss.pop();
		Address s1 = ss.pop();
		if (s1.getVarType() != VarType.BOOL) {
			ErrorHandler.printError("In not operator the operand must be boolean");
		}
		memory.add3AddressCode(Operation.NOT, s1, s2, temp);
		ss.push(temp);

	}

	public void defClass() {
		ss.pop();
		symbolTable.addClass(symbolStack.peek());
	}

	public void defMethod() {
		ss.pop();
		String methodName = symbolStack.pop();
		String className = symbolStack.pop();

		symbolTable.addMethod(className, methodName, memory.getCurrentCodeBlockAddress());

		symbolStack.push(className);
		symbolStack.push(methodName);

	}

	public void popClass() {
		symbolStack.pop();
	}

	public void extend() {
		ss.pop();
		symbolTable.setSuperClass(symbolStack.pop(), symbolStack.peek());
	}

	public void defField() {
		ss.pop();
		symbolTable.addField(symbolStack.pop(), symbolStack.peek());
	}

	public void defVar() {
		ss.pop();

		String var = symbolStack.pop();
		String methodName = symbolStack.pop();
		String className = symbolStack.pop();

		symbolTable.addMethodLocalVariable(className, methodName, var);

		symbolStack.push(className);
		symbolStack.push(methodName);
	}

	public void methodReturn() {
		//TODO : call ok

		String methodName = symbolStack.pop();
		Address s = ss.pop();
		SymbolType symbolType = symbolTable.getMethodReturnType(symbolStack.peek(), methodName);
		VarType temp = extractVarType(symbolType);
		if (s.getVarType() != temp) {
			ErrorHandler.printError("The type of method and return address was not match");
		}
		memory.add3AddressCode(Operation.ASSIGN, s, new Address(symbolTable.getMethodReturnAddress(symbolStack.peek(), methodName), VarType.ADDRESS, TypeAddress.INDIRECT), null);
		memory.add3AddressCode(Operation.JP, new Address(symbolTable.getMethodCallerAddress(symbolStack.peek(), methodName), VarType.ADDRESS), null, null);

		//symbolStack.pop();

	}

	public void defParam() {
		//TODO : call Ok
		ss.pop();
		String param = symbolStack.pop();
		String methodName = symbolStack.pop();
		String className = symbolStack.pop();

		symbolTable.addMethodParameter(className, methodName, param);

		symbolStack.push(className);
		symbolStack.push(methodName);
	}

	public void lastTypeBool() {
		symbolTable.setLastType(SymbolType.BOOL);
	}

	public void lastTypeInt() {
		symbolTable.setLastType(SymbolType.INT);
	}


	private void checkIntOperandsForOperation(Address s2, Address s1, String operation) {
		if (s1.getVarType() != VarType.INT || s2.getVarType() != VarType.INT) {
			ErrorHandler.printError("In " + operation + " two operands must be integer");
		}
	}

	private void checkSameOperandForOperation(Address s2, Address s1, String operation) {
		if (s1.getVarType() != s2.getVarType()) {
			ErrorHandler.printError("The type of operands in " + operation + " operator is different");
		}
	}

	private VarType extractVarType(SymbolType st) {
		switch (st) {
			case BOOL:
				return VarType.BOOL;
			case INT: //keep it for readability
				return VarType.INT;
			default:
				return VarType.INT;
		}
	}

}
