package com.lendamage.agilegtd.model;

/**
 *  Common model entity.
 */
interface Entity<EditorType extends Entity.Editor> {

    /**
     *  Creates new editor for the entity.
     */
    EditorType edit();
    
    /**
     *  Editor of the entity.
     */
    public interface Editor {
        
        /**
         *  Commits changes in the entity.
         */
        void commit();
        
    }
    
}
