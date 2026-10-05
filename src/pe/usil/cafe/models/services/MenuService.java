package pe.usil.cafe.models.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Devuelve los módulos del menú que le corresponden a cada rol. */
public class MenuService {

    public static class Modulo {
        public final String codigo;
        public final String nombre;
        public final boolean disponible;

        Modulo(String codigo, String nombre, boolean disponible) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.disponible = disponible;
        }
    }

    // {codigo, nombre, roles permitidos separados por coma}
    private static final String[][] CATALOGO = {
        {"lotes",         "Mis lotes",               "CAFICULTOR"},
        {"marketplace",   "Explorar lotes",          "COMPRADOR"},
        {"ventas",        "Ventas",                  "CAFICULTOR,ADMINISTRADOR"},
        {"compras",       "Compras",                 "COMPRADOR,ADMINISTRADOR"},
        {"microcreditos", "Micropréstamos",          "CAFICULTOR,ADMINISTRADOR"},
        {"caficultores",  "Caficultores",            "ADMINISTRADOR"},
        {"compradores",   "Compradores",             "ADMINISTRADOR"},
        {"reportes",      "Reportes",                "ADMINISTRADOR"},
        {"usuarios",      "Usuarios",                "ADMINISTRADOR"},
        {"seguridad",     "Seguridad y auditoría",   "ADMINISTRADOR"}
    };

    // A medida que termines un módulo, agrega su código aquí.
    private static final Set<String> DISPONIBLES = new HashSet<>();

    public List<Modulo> modulosPara(String rol) {
        List<Modulo> lista = new ArrayList<>();
        for (String[] m : CATALOGO) {
            if (Arrays.asList(m[2].split(",")).contains(rol)) {
                lista.add(new Modulo(m[0], m[1], DISPONIBLES.contains(m[0])));
            }
        }
        return lista;
    }
}
