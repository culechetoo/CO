//DOUBT R1 , R2 ,RD values order in CMP,CMN etc. ADC

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
	public static void main(String[] args) {
		init();
		
		/*
		Opcodes=new HashMap<String,String>();
		Opcodes.put("0000","AND"); 
		Opcodes.put("0001","EOR"); 
		Opcodes.put("0010","SUB"); d
		Opcodes.put("0011","RSB");
		Opcodes.put("0100","ADD"); d
		Opcodes.put("0101","ADC"); 
		Opcodes.put("0110","SBC");
		Opcodes.put("0111","RSC");
		Opcodes.put("1000","TST");
		Opcodes.put("1001","TEQ");
		Opcodes.put("1010","CMP"); d
		Opcodes.put("1011","CMN"); d
		Opcodes.put("1100","ORR");
		Opcodes.put("1101","MOV"); d
		Opcodes.put("1110","BIC"); 
		Opcodes.put("1111","MVN"); d
		Registers=new HashMap<String,Register>();
		Registers.put("0000",new Register("R0",0L));
		Registers.put("0001",new Register("R1",0L));
		Registers.put("0010",new Register("R2",0L));
		Registers.put("0011",new Register("R3",0L));
		Registers.put("0100",new Register("R4",0L));
		Registers.put("0101",new Register("R5",0L));
		Registers.put("0110",new Register("R6",0L));
		Registers.put("0111",new Register("R7",0L));
		Registers.put("1000",new Register("R8",0L));
		Registers.put("1001",new Register("R9",0L));
		Registers.put("1010",new Register("R10",0L));
		Registers.put("1011",new Register("R11",0L));
		Registers.put("1100",new Register("R12",0L));
		Registers.put("1101",new Register("R13(sp)",0L));
		Registers.put("1110",new Register("R14(lr)",0L));
		Registers.put("1111",new Register("R15(pc)",0L));
		 
		try
		{   
			FileOutputStream file = new FileOutputStream("OPCODES.file");
			ObjectOutputStream o=new ObjectOutputStream(file);
			o.writeObject(Opcodes);
			file.close();
			FileOutputStream file2 = new FileOutputStream("REGISTERS.file");
			ObjectOutputStream o2=new ObjectOutputStream(file2);
			o2.writeObject(Registers);
			file2.close();	
		}         
		catch(IOException ex)
		{System.out.println(ex.getMessage());}
		 */
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
		ArrayList<Instruction> ins=new ArrayList<Instruction>();
		
		String fileName = "input.MEM";
		String line=null;
		try {
			BufferedReader b=new BufferedReader(new FileReader(fileName));
			while((line=b.readLine())!=null) {
				String[] str=line.split(" ");
				Instruction i=new Instruction(str[0],str[1]);
				ins.add(i);
				System.out.println("Fetch instruction "+str[1]+" from address "+str[0]);
				Decode(i);
			}   
			b.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '"+fileName+"'");                
		}
		catch(IOException ex) {
			System.out.println("Error reading file '"+ fileName + "'");                  
		}
		return ins;
	}
	
	private static void Decode(Instruction instr) {
		System.out.println("DECODE:");
		String value = new BigInteger(instr.Value.substring(2), 16).toString(2);
		String bin=String.format("%32s", value).replace(" ", "0");
		
		Register R1=null;
		Register R2=null;
		Register R3=null;
		String Read="";
		//String binrev=new StringBuilder(bin).reverse().toString();
		//System.out.println(binrev);
		
		//System.out.println(bin);
		String Cond=bin.substring(0,4);
		String I=bin.substring(6,7);
		String Opcode=bin.substring(7,11);
		String S=bin.substring(11,12);
		String Rn=bin.substring(12,16);
		String Rd=bin.substring(16,20);
		R1=Registers.get(Rn);
		R3=Registers.get(Rd);
		Read+="Read Registers: "+R1.show();
		/*
		System.out.println(Cond);
		System.out.println(I);
		System.out.println(Opcode);
		System.out.println(S);
		System.out.println(Rn);
		System.out.println(Rd);
		*/
		String Imm="";
		String Rm=null;
		System.out.print("Operation is "+Opcodes.get(Opcode));
		System.out.print(" , First Operand is "+Registers.get(Rn));
		if(I.equals("0")) {
			String shift=bin.substring(20,28);
			Rm=bin.substring(28,32);
			R2=Registers.get(Rm);
			Read+=", "+R2.show();
			//System.out.println(shift);
			//System.out.println(Rm);
			System.out.print(" , Second Operand is "+Registers.get(Rm));
		}else
		{
			String Rotate=bin.substring(20,24);
			Imm=bin.substring(24,32);
			//System.out.println(Rotate);
			//System.out.println(Imm);
			System.out.print(" , immediate Second Operand is "+Integer.parseInt(Imm, 2));
			//System.out.println("HELOLLLOOO"+Imm+"HELLO");
		}
		System.out.println(" ,Destination Register is "+Registers.get(Rd)+".");
		
		System.out.println(Read);
		
		// Pass all elements some maybe null according to instruction type- handled in execute
		if(Imm!="")    
		{
			Execute(Opcode,Registers.get(Rn),Registers.get(Rm),Integer.parseInt(Imm, 2),Registers.get(Rd));
			//System.out.println(Opcode+" "+Registers.get(Rn)+" "+Registers.get(Rm)+" "+Integer.parseInt(Imm, 2)+" "+Registers.get(Rd));
		}
		else
		{
			Execute(Opcode,Registers.get(Rn),Registers.get(Rm),null,Registers.get(Rd));
			//System.out.println(Opcode+" "+Registers.get(Rn)+" "+Registers.get(Rm)+" "+"NULL"+" "+Registers.get(Rd));
		}
				
		Mem();
		Writeback(); 
		
	}
	
	private static void Execute(String OC, Register R1, Register R2, Integer Imm,Register RD) 
	{
		System.out.println("EXECUTE:");
		if(Opcodes.get(OC).equals("ADD"))
		{
			if(R1!=null && R2!=null && RD!=null)
			{
				RD.Value=R1.Value+R2.Value;
			}	
			else
			{
				RD.Value=R1.Value-Long.valueOf(Imm.longValue());
			}
		}
		if(Opcodes.get(OC).equals("SUB"))
		{
			if(R1!=null && R2!=null && RD!=null)
			{
				RD.Value=R1.Value-R2.Value;
			}	
			else
			{
				RD.Value=R1.Value-Long.valueOf(Imm.longValue());
			}
		}
		if(Opcodes.get(OC).equals("CMP"))
		{
			if(R1!=null && R2!=null)
			{
				//return RD.Value-R1.Value;
			}	
			else
			{
				//return RD.Value-Long.valueOf(Imm.longValue());
			}
		}
		if(Opcodes.get(OC).equals("CMN"))
		{
			if(R1!=null && R2!=null)
			{
				// return RD.Value+R1.Value;
			}	
			else
			{
				// return RD.Value+Long.valueOf(Imm.longValue());
			}
		}
		if(Opcodes.get(OC).equals("MOV"))
		{
			if(R1!=null && R2!=null)
			{
				RD.Value=R1.Value;
			}	
			else
			{
				RD.Value=Long.valueOf(Imm.longValue());
			}
		}
		if(Opcodes.get(OC).equals("MVN"))
		{
			if(R1!=null && R2!=null)
			{
				String bin = Long.toBinaryString(~R1.Value);  //complement
				RD.Value=Long.valueOf(bin);
			}	
			else
			{
				String bin = Long.toBinaryString(~Long.valueOf(Imm.longValue()));
				RD.Value=Long.valueOf(bin);
			}
		}

		
		
		 
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
			
	}
	
	private static void Mem() {
		System.out.println("MEMORY:");
		
	}
	
	private static void Writeback() {
		System.out.println("WRITEBACK:");
		
	}
}

class Instruction{
	String Address;
	String Value;
	public Instruction(String string, String string2) {
		Address=string;
		Value=string2;
	}
}

class Register implements Serializable{
	private static final long serialVersionUID = 1L;
	String Name;
	Long Value;
	public Register(String string, Long val) {
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

