package com.aidan.coursework.bookshop.gui;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Arrays;

// We need a custom class here to allow only 1 column (quantity) to be selectable
// it can support multiple columns because it felt bad to only have 1 for no apparent reason
public class RestrictedTableModel extends DefaultTableModel {
	
	List<Integer> editable_cols;
	TableValidatorFunction callback;
	
    public RestrictedTableModel(Object[][] data, Object[] columnNames, Integer[] editable_cols, TableValidatorFunction validator_callback) {
        super(data, columnNames);
        this.editable_cols = Arrays.asList(editable_cols);
        this.callback = validator_callback;
    }
    
    public boolean isCellEditable(int row, int col) {
       return editable_cols.contains(col);
       
    }
    
    @Override
    public void setValueAt(Object aValue, int row, int column) {
    	// call the callback TO SEE IF THE EDIT IS VALID !
    	if (!this.callback.apply(aValue, row, column)) {
    		return;
    	};
    	super.setValueAt(aValue, row, column);
    }
}
