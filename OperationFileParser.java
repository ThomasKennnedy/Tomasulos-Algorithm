import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

///
/// This class provides all file parsing funtionality. Given an input file, one OperationList and one RegisterFiles  
/// will be generated.

public class OperationFileParser{
     private String filename; ///< The input filename
     
     ///
     ///Set the input file to the default value of "a.in".
     ///     
     public OperationFileParser(){
          this.filename = "a.in";
     }
     
     ///
     ///Set the input file to the specified filename.
     ///
     public OperationFileParser( String filename ){
          this.filename = filename;
     }
     
     ///
     /// Parse the input file; generate one OperationList and one RegisterFiles.
     ///
     public void parseFile( OperationList oplist, RegisterFiles reg_files ) throws Exception{
          FileReader in_file = new FileReader( filename );
          BufferedReader file_buff = new BufferedReader( in_file );
          
          String[] split_1, split_2, operands;
          String line, operation;
          
          while( ( line = file_buff.readLine() ) != null){
               split_1 = (line.trim()).split(";"); 

               //Split/Prse the operation & operands
               split_2 = (split_1[0].trim()).split("\\s+");
               operation = split_2[0];

               operands = new String[ (split_2.length - 1) ];

               for( int i = 1; i < split_2.length; i++){
                    operands[ i-1 ] = split_2[i].substring( 0, split_2[i].indexOf(",") );
               }
               
               if( operands.length == 3 ){
                    oplist.addOperation( new Operation( operation, operands[0], operands[1], operands[2], split_1[1] ) ); 
               }
               else if( operands.length == 2 ){
                    oplist.addOperation( new Operation( operation, operands[0], operands[1], split_1[1] ) ); 
               }
               else{
                    throw new Exception(){
                         public String toString(){
                              return "File Parse Error: Malformed Operation";  
                         }
                    };  
               }
               //Parse the comment
              
          }          
          
     }
     
     ///
     ///Generates the string representation of the OperationFileParser Object.
     ///
     public String toString(){
          return "Operation File Parser";
     }
}
