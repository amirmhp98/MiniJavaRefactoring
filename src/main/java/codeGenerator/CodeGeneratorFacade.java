package codeGenerator;

import scanner.token.Token;

public class CodeGeneratorFacade {

	private CodeGenerator codeGenerator;

	public CodeGeneratorFacade() {
		this.codeGenerator = new CodeGenerator();
	}

	public void printMemory(){
		codeGenerator.printMemory();
	}

	public void semanticFunction(int func, Token next){
		codeGenerator.semanticFunction(func, next);
	}

}
