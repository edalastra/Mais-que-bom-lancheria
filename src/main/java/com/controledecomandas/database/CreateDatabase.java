package com.controledecomandas.database;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CreateDatabase {

    private final PostgresConnection postgresConnection;

    public CreateDatabase(PostgresConnection postgresConnection) {
        this.postgresConnection = postgresConnection;
    }

    public void createTables()  {

        boolean connected = this.postgresConnection.connect();

        try {
             System.out.println("creating database...");
            this.importSQL(postgresConnection,
                    getClass().getResourceAsStream("/database/createdatabase.sql"));

        } catch (SQLException  e) {
            System.out.println(e);

        } finally {
            if(connected){
                this.postgresConnection.desconnect();
            }
        }

    }

    private static void importSQL(PostgresConnection conn, InputStream in) throws SQLException
    {
        Scanner s = new Scanner(in);
        s.useDelimiter("(;(\r)?\n)|(--\n)");
        Statement st = null;
        try
        {
            st = conn.createStatement();
            while (s.hasNext())
            {
                String line = s.next();
                if (line.startsWith("/*!") && line.endsWith("*/"))
                {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " */".length());
                }

                if (line.trim().length() > 0)
                {
                    st.execute(line);
                }
            }
        }
        finally
        {
            if (st != null) st.close();
        }
    }
}

