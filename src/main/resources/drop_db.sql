DECLARE
	v_sql                    VARCHAR2(32757);
BEGIN
	FOR cr in (select table_name from user_tables)
	LOOP
		v_sql := 'drop table '||cr.table_name||' cascade constraints';
		EXECUTE IMMEDIATE v_sql;
	END LOOP;
	
	FOR cr in (select sequence_name from user_sequences) 
	LOOP		
		v_sql := 'drop sequence '||cr.sequence_name;
		execute immediate v_sql;
	END LOOP;

	FOR cr in (select view_name from user_views)
	LOOP
		v_sql := 'drop view '||cr.view_name||' cascade constraints';
		execute immediate v_sql;
	END LOOP;

	FOR cr in (select OBJECT_NAME from user_objects where object_type = 'FUNCTION') 
	LOOP
		v_sql := 'drop function ' || cr.Object_name;
		execute immediate v_sql;
	END LOOP;
	
	FOR cr in (select OBJECT_NAME from user_objects where object_type = 'PROCEDURE') 
	LOOP
		v_sql := 'drop PROCEDURE ' || cr.Object_name;
		execute immediate v_sql;
	END LOOP;

	FOR cr in (select OBJECT_NAME from user_objects where object_type = 'PACKAGE') 
	LOOP
		v_sql := 'drop PACKAGE ' || cr.Object_name;
		execute immediate v_sql;
	END LOOP;

	FOR cr in (select OBJECT_NAME from user_objects where object_type = 'TRIGGER') 
	LOOP
		v_sql := 'drop TRIGGER ' || cr.Object_name;
		execute immediate v_sql;
	END LOOP;

	FOR cr in (select OBJECT_NAME from user_objects where object_type = 'TYPE') 
	LOOP
		v_sql := 'drop TYPE ' || cr.Object_name || ' force';
		execute immediate v_sql;
	END LOOP;

	FOR cr in (select OBJECT_NAME from user_objects where object_type = 'JAVA SOURCE') 
	LOOP
		v_sql := 'drop java source ' ||'"'||cr.Object_name||'"';
		execute immediate v_sql;
	END LOOP;
	COMMIT;
END;
/
