/**
 * 
 */
/**
 * 
 */
module DataLeague {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
    requires java.sql;
    
    opens Vista to javafx.graphics;
    opens ProgramaPrincipal to javafx.graphics;
    
    opens ClasesObjetos to javafx.base;
}