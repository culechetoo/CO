//DOUBT R1 , R2 ,RD values order in CMP,CMN etc. ADC

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class ARMDecoder {

	ArrayList<String> Instructions;
	static HashMap<String,String> Opcodes;
	static HashMap<String,Register> Registers;
	static ArrayList<Instruction> ins;
	static int[] mem = new int[4096];
	public static int n = 0;
	public static int z = 0;
	public static int c = 0;
	public static int v = 0;
	private static boolean finished=false;
	static int PC;
	static BufferedReader reader;
	public static void main(String[] args) {
		reader= new BufferedReader(new InputStreamReader(System.in));
		init();
//		System.out.println(Registers.get("0010").Value);
//		Opcodes=new HashMap<String,String>();
//		Opcodes.put("0000","AND"); 
//		Opcodes.put("0001","EOR"); 
//		Opcodes.put("0010","SUB"); 
//		Opcodes.put("0011","RSB"); 
//		Opcodes.put("0100","ADD"); 
//    	Opcodes.put("0101","ADC"); 
//		Opcodes.put("0110","SBC");
//		Opcodes.put("0111","RSC");
//		Opcodes.put("1000","TST");
//		Opcodes.put("1001","TEQ");
//		Opcodes.put("1010","CMP"); 
//		Opcodes.put("1011","CMN"); 
//		Opcodes.put("1100","ORR");  
//		Opcodes.put("1101","MOV"); 
//		Opcodes.put("1110","BIC"); 
//		Opcodes.put("1111","MVN"); 
//		Registers=new HashMap<String,Register>();
//		Registers.put("0000",new Register("R0",0));
//		Registers.put("0001",new Register("R1",0));
//		Registers.put("0010",new Register("R2",0));
//		Registers.put("0011",new Register("R3",0));
//		Registers.put("0100",new Register("R4",0));
//		Registers.put("0101",new Register("R5",0));
//		Registers.put("0110",new Register("R6",0));
//		Registers.put("0111",new Register("R7",0));
//		Registers.put("1000",new Register("R8",0));
//		Registers.put("1001",new Register("R9",0));
//		Registers.put("1010",new Register("R10",0));
//		Registers.put("1011",new Register("R11",0));
//		Registers.put("1100",new Register("R12",0));
//		Registers.put("1101",new Register("R13(sp)",0));
//		Registers.put("1110",new Register("R14(lr)",0));
//		Registers.put("1111",new Register("R15(pc)",0));
//		 
//		try
//		{   
//			FileOutputStream file = new FileOutputStream("OPCODES.file");
//			ObjectOutputStream o=new ObjectOutputStream(file);
//			o.writeObject(Opcodes);
//			file.close();
//			FileOutputStream file2 = new FileOutputStream("REGISTERS.file");
//			ObjectOutputStream o2=new ObjectOutputStream(file2);
//			o2.writeObject(Registers);
//			file2.close();	
//		}         
//		catch(IOException ex)
//		{System.out.println(ex.getMessage());}
		 
	}
	
	@SuppressWarnings("unchecked")
	private static void init() {

		try
		{  
			
			FileInputStream file = new FileInputStream("OPCODES.file");
			ObjectInputStream o=new ObjectInputStream(file);
			Opcodes=(HashMap<String,String>)o.readObject();
			file.close();	
			
			FileInputStream file2 = new FileInputStream("REGISTERS.file");
			ObjectInputStream o2=new ObjectInputStream(file2);
			Registers=(HashMap<String,Register>)o2.readObject();
			file2.close();
			
			ins=new ArrayList<Instruction>();
		}         
		catch(IOException ex){
			System.out.println(ex.getMessage());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
		
		Fetch();
	}
	
	private static ArrayList<Instruction> Fetch() {
		String fileName = "input.MEM";
		String line=null;
		try {
			BufferedReader b=new BufferedReader(new FileReader(fileName));
			while((line=b.readLine())!=null) {
				String[] str=line.split(" ");
				Instruction i=new Instruction(str[0],str[1]);
				ins.add(i);
			}   
			b.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '"+fileName+"'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '"+ fileName + "'");                  
		}
		PC=0;
		while(!finished) {
			System.out.println();
			System.out.println("FETCH:\nInstruction "+ins.get(PC).Value+" from address "+ins.get(PC).Address);
			Decode(ins.get(PC));
		}
		return ins;
	}
	
	private static void Decode(Instruction instr) {
		Registers.get("1111").Value=PC;
		if(instr.Value.equals("0xEF000011")) {
			finished = true;
			System.out.println("MEMORY: No memory operation\nEXIT");
		}
		else {
			System.out.println("DECODE:");
			String value = new BigInteger(instr.Value.substring(2), 16).toString(2);
			String bin=String.format("%32s", value).replace(" ", "0");
			boolean execute = true;
			String flags=bin.substring(0,4);
			if(flags.equals("0000")){
				if(z==1) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			else if(flags.equals("0001")){
				if(z==0) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			else if(flags.equals("0010")){
				if(c==1) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			else if(flags.equals("0011")){
				if(c==0) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			else if(flags.equals("0100")){
				if(n==1) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			else if(flags.equals("0101")){
				if(n==0) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			else if(flags.equals("0110")){
				if(v==1) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			else if(flags.equals("0111")){
				if(v==0) {
					execute = true;
				}
				else{
					execute = false;
				}
			}		
			else if(flags.equals("1000")){
				if(c==1&&z==0) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			else if(flags.equals("1001")){
				if(c==0||z==1) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			else if(flags.equals("1010")){
				if(n==v) {
					execute = true;
				}
				else{
					execute = false;
				}
			}	
			else if(flags.equals("1011")){
				if(n!=v) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			else if(flags.equals("1100")){
				if(z==0&&n==v) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			else if(flags.equals("1101")){
				if(z==1||n!=v) {
					execute = true;
				}
				else{
					execute = false;
				}
			}
			if(execute) {
				
				if(bin.substring(4,6).equals("00")) {
					PC++;
					Register R1;
					String Read="";
					//String binrev=new StringBuilder(bin).reverse().toString();
					//System.out.println(binrev);
					
					//System.out.println(bin.substring(8,32).length());
					String I=bin.substring(6,7);
					String Opcode=bin.substring(7,11);
					String S=bin.substring(11,12);
					String Rn=bin.substring(12,16);
					String Rd=bin.substring(16,20);
					R1=Registers.get(Rn);
					String Rm = bin.substring(28,32);
					Register R2 = new Register(Registers.get(Rm).Name, Registers.get(Rm).Value);
					Read+="Read Registers: "+Registers.get(Rn).show();
					/*
					System.out.println(Cond);
					System.out.println(I);
					System.out.println(Opcode);
					System.out.println(S);
					System.out.println(Rn);
					System.out.println(Rd);
					*/
					String Imm="";
					System.out.print("Operation is "+Opcodes.get(Opcode));
					System.out.print(" , First Operand is "+Registers.get(Rn));
					int a = (new Operand2Calculator(bin.substring(20,32), I)).calcoperand();
					if(I.equals("0")) 
					{
						Read+=", "+Registers.get(Rm).show();
						System.out.print(" , Second Operand is "+Registers.get(Rm));
					}
					else
					{
						Imm=bin.substring(24,32);
						System.out.print(" , immediate Second Operand is "+a);
					}
					System.out.println(" ,Destination Register is "+Registers.get(Rd)+".");
					
					System.out.println(Read);
					
					// Pass all elements some maybe null according to instruction type- handled in execute
					if(Imm!="")    
					{
						Execute(Opcode,R1,null,Integer.parseInt(Imm, 2),Registers.get(Rd),S);
						//System.out.println(Opcode+" "+Registers.get(Rn)+" "+Registers.get(Rm)+" "+Integer.parseInt(Imm, 2)+" "+Registers.get(Rd));
					}
					else
					{
						Execute(Opcode,R1,R2,null,Registers.get(Rd), S);
						//System.out.println(Opcode+" "+Registers.get(Rn)+" "+Registers.get(Rm)+" "+"NULL"+" "+Registers.get(Rd));
					}
				}
				else if(bin.substring(4,7).equals("101"))
				{
					String Offset = bin.substring(8,32);
					
					int L=Integer.parseInt(bin.substring(7,8));
					System.out.println("Operation is BRANCH, Link = "+L+", Offset = "+getTwosComplement(Offset));
					Branch(L,Offset);
				}
				else if(bin.substring(4,6).equals("01")) {
					System.out.println("Memory Function");
					PC++;
					System.out.print("Memory: ");
					String mode = bin.substring(11, 16);
					int a = Integer.parseInt(mode, 2);
					String offset = bin.substring(20,32);
					int off;
					if(bin.substring(6).equals("0")) {
						off = Integer.parseInt(bin.substring(20, 32), 2);
					}
					else {
						off = (new Operand2Calculator(offset, "0")).calcoperand();
					}
					if(a==24) {
						String Rn = bin.substring(12,16);
						mem[off] = Registers.get(Rn).Value;
						System.out.println(mem[off]+" written to "+off);
					}
					else if(a==25){
						String Rd = bin.substring(16,20);
						System.out.println(mem[off]+" from "+off+" written to Register "+Registers.get(Rd).Name);
						Writeback(Registers.get(Rd), mem[off]);
					}
				}
				else if(bin.substring(4,6).equals("1111")) {
					if(bin.endsWith("6B")|bin.endsWith("6b")) {
						System.out.println("Instruction is write int.\nRead Registers:"+Registers.get("0001").show()+"\nEXECUTE:");
						if(Registers.get("0000").Value==1)
							System.out.println("Writing to Console: "+Registers.get("0001").Value);
						System.out.println("MEMORY:\nNo memory operation.\nWRITEBACK:\nNo Writeback");
					}
					else if(bin.endsWith("6c")|bin.endsWith("6C")) {
						System.out.println("Instruction is read int \nEXECUTE:");
						int n=0;
						System.out.println("Reading from Console: ");
						if(Registers.get("0000").Value==0)
							try {n=Integer.parseInt(reader.readLine());}catch (NumberFormatException | IOException e ) {e.printStackTrace();}
						System.out.println("MEMORY:\nNo memory operation");
						Writeback(Registers.get("0000"),n);
					}
					else if(bin.endsWith("00")) {
						System.out.println("Instruction is write char.\nRead Registers:"+Registers.get("0000").show()+"\nEXECUTE:");
						System.out.println("Writing to Console: "+(char)Registers.get("0000").Value);
						System.out.println("MEMORY:\nNo memory operation.\nWRITEBACK:\nNo Writeback");
					}
				}
			}
			else {
				PC++;
				System.out.println("Instruction not executed as conditions failed.");
			}
		}
		
	}
	
	private static void Branch(int l, String offset) {
		String bin=String.format("%32s", offset).replace(" ", "0");
		//System.out.println(Registers.get("1111").Value+" "+getTwosComplement(bin)+" "+getTwosComplement(offset));
		System.out.println("EXECUTE:");
		if(l==1) 
			Registers.get("1110").Value=PC;
			
		int address=PC+getTwosComplement(offset)+2;
		PC=address;
		System.out.println("BRANCH to address PC+4("+4*getTwosComplement(offset)+")");
		System.out.println(address);
		Instruction i=ins.get(PC);
		System.out.println("FETCH: instruction "+i.Value+" from address "+i.Address);
		Decode(i);
	}
	
	public static int getTwosComplement(String binaryInt) {
	    //Check if the number is negative.
	    //We know it's negative if it starts with a 1
	    if (binaryInt.charAt(0) == '1') {
	        //Call our invert digits method
	        String invertedInt = invertDigits(binaryInt);
	        //Change this to decimal format.
	        int decimalValue = Integer.parseInt(invertedInt, 2);
	        //Add 1 to the curernt decimal and multiply it by -1
	        //because we know it's a negative number
	        decimalValue = (decimalValue + 1) * -1;
	        //return the final result
	        return decimalValue;
	    } else {
	        //Else we know it's a positive number, so just convert
	        //the number to decimal base.
	        return Integer.parseInt(binaryInt, 2);
	    }
	}

	public static String invertDigits(String binaryInt) {
	    String result = binaryInt;
	    result = result.replace("0", " "); //temp replace 0s
	    result = result.replace("1", "0"); //replace 1s with 0s
	    result = result.replace(" ", "1"); //put the 1s back in
	    return result;
	}
	
	private static void Execute(String OC, Register R1, Register R2, Integer Imm,Register RD, String s) 
	{
		System.out.println("EXECUTE:");
		if(Opcodes.get(OC).equals("ADD"))
		{
			if(R1!=null && R2!=null && RD!=null)
			{
				int h=R1.Value+R2.Value;
				System.out.println("Adding "+R1.Value+" "+R2.Value);
				Writeback(RD,h);
			}	
			else
			{
				int h=R1.Value+Imm;
				System.out.println("Adding "+R1.Value+" and "+Imm);
				Writeback(RD,h);
			}
			if(s.equals("1"))
			{
				if(RD.Value<0)
				{
					n=1;
					z=0;
				}
				else if(RD.Value==0)
				{
					z=1;
					n=0;	
				}
				else
				{
					z=0;
					n=0;
				}
				
			}
		}
		if(Opcodes.get(OC).equals("SUB"))
		{
			if(R1!=null && R2!=null && RD!=null)
			{
				int h=R1.Value-R2.Value;
				System.out.println("Subtracting "+R2.Value+" from "+R1.Value);
				Writeback(RD,h);
			}	
			else
			{
				int h=R1.Value-Imm;
				System.out.println("Subtracting "+Imm+" from "+R1.Value);
				Writeback(RD,h);
			}
			if(s.equals("1"))
			{
				if(RD.Value<0)
				{
					n=1;
					z=0;
				}
				else if(RD.Value==0)
				{
					z=1;
					n=0;	
				}
				else
				{
					z=0;
					n=0;
				}
				
			}
		}
		if(Opcodes.get(OC).equals("RSB"))
		{
			if(R1!=null && R2!=null && RD!=null)
			{
				int h=R2.Value-R2.Value;
				System.out.println("Subtracting "+R1.Value+" from "+R2.Value);
				Writeback(RD,h);
			}	
			else
			{
				int h=Imm-R1.Value;
				System.out.println("Subtracting "+R1.Value+" from "+Imm);
				Writeback(RD,h);
			}
		}
		if(Opcodes.get(OC).equals("CMP"))
		{
			if(R1!=null && R2!=null)
			{
				if(R1.Value<R2.Value)
				{
					n=1;
					z=0;
				}
				else if(R1.Value==R2.Value)
				{
					z=1;
					n=0;
				}
				else
				{
					z=0;
					n=0;
				}
				System.out.println("Comparing "+R1.Value+" and "+R2.Value);
			}	
			else
			{
				if(R1.Value<Imm)
				{
					n=1;
					z=0;
				}
				else if(R1.Value==Imm)
				{
					z=1;
					n=0;
				}
				else
				{
					z=0;
					n=0;
					
				}
				System.out.println("Comparing "+R1.Value+" and "+Imm);
			}
		}
		
		
		if(Opcodes.get(OC).equals("CMN"))
		{
			if(R1!=null && R2!=null)
			{
				if(R1.Value+R2.Value<0)
				{
					n=1;
					z=0;
				}
				else if(R1.Value+R2.Value==0)
				{
					z=1;
					n=0;
				}
				else
				{
					z=0;
					n=0;
					
				}
				System.out.println("Not Comparing "+R1.Value+" and "+R2.Value);
			}	
			else
			{
				if(R1.Value+Imm<0)
				{
					n=1;
					z=0;
				}
				else if(R1.Value+Imm==0)
				{
					z=1;
					n=0;
				}
				else
				{
					z=0;
					n=0;
					
				}
				System.out.println("Not Comparing "+R1.Value+" and "+Imm);
			}
		}
		if(Opcodes.get(OC).equals("MOV"))
		{
			if(R1!=null && R2!=null)
			{
				System.out.println("Moving "+R2.Value+" to "+RD.Name);
				Writeback(RD,R2.Value);
				
			}	
			else
			{
				System.out.println("Moving "+Imm+" to "+RD.Name);
				Writeback(RD,Imm);
			}
		}
		if(Opcodes.get(OC).equals("MVN"))
		{
			if(R1!=null && R2!=null)
			{
				String bin = Integer.toBinaryString(~R2.Value);  //complement
				System.out.println("Moving "+R2.Value+" to "+RD.Name);
				Writeback(RD,Integer.parseInt(bin, 2));
			}	
			else
			{
				String bin = Integer.toBinaryString(~Imm);
				System.out.println("Not Moving "+Imm+" to "+RD.Name);
				Writeback(RD,Integer.parseInt(bin, 2));
			}
		}
		if(Opcodes.get(OC).equals("EOR"))
		{
			if(R1!=null && R2!=null && RD!=null)
			{
				System.out.println("Exor "+R1.Value+" and "+ R2.Value);
				Writeback(RD,R1.Value^R2.Value);
			}	
			else
			{
				System.out.println("Exor "+R1.Value+" and "+ Imm);
				Writeback(RD,R1.Value^Imm);
			}
		}
		if(Opcodes.get(OC).equals("ORR"))
		{
			if(R1!=null && R2!=null && RD!=null)
			{
				System.out.println("OR "+R1.Value+" and "+ R2.Value);
				Writeback(RD,R1.Value  | R2.Value);
			}	
			else
			{
				System.out.println("OR "+R1.Value+" and "+ R2.Value);
				Writeback(RD,R1.Value | Imm);
			}
		}
		if(Opcodes.get(OC).equals("AND"))
		{
			if(R1!=null && R2!=null && RD!=null)
			{
				System.out.println("And "+R1.Value+" and "+ R2.Value);
				Writeback(RD,R1.Value & R2.Value);
			}	
			else
			{
				System.out.println("And "+R1.Value+" and "+ R2.Value);
				Writeback(RD,R1.Value & Imm);
			}
		}
		if(Opcodes.get(OC).equals("BIC"))
		{
			if(R1!=null && R2!=null && RD!=null)
			{
				String h=Integer.toBinaryString(~R2.Value);
				int g=Integer.parseInt(h, 2);
				System.out.println("BIC "+R1.Value+" and "+ R2.Value);
				Writeback(RD,R1.Value & g);
			}	
			else
			{
				String h=Integer.toBinaryString(~Imm);
				int g=Integer.parseInt(h, 2);
				System.out.println("BIC "+R1.Value+" and "+ R2.Value);
				Writeback(RD,R1.Value & g);
			}
		}		
	}
	private static void Writeback(Register Rd, int result) 
	{
		Rd.Value=result;
		System.out.println("WRITEBACK: "+result+" written to "+Rd.Name);
	}
		
}

class Instruction{
	String Address;
	String Value;
	int N;
	int Z;
	int V;
	int C;
	public Instruction(String string, String string2) {
		Address=string;
		Value=string2;
	}
}

class Register implements Serializable{
	private static final long serialVersionUID = 1L;
	String Name;
	int Value;
	public Register(String string, int val) {
		Name=string;
		Value=val;
	}
	
	public String show() {
		return Name+" = "+String.valueOf(Value);
	}

	public String toString() {
		return Name;
	}
}
