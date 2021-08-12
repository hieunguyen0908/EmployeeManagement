/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hieunnm.daos;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import hieunnm.dtos.EmpDTO;
import hieunnm.utils.DBHelpers;
import java.math.BigInteger;

/**
 *
 * @author PC
 */
public class EmpDAO implements Serializable {

    private final String DATE_FORMAT = "MM/dd/yyyy";

    public boolean createEmp(EmpDTO dto) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Insert Into tblEmployees (empID, fullname, phone, email, "
                        + "address, dateOfBirth, isDelete) Values (?,?,?,?,?,?,0)";
                ps = con.prepareStatement(sql);
                String empID = dto.getEmpID();
                String fullName = dto.getFullName();
                String phone = dto.getPhone();
                String email = dto.getEmail();
                String address = dto.getAddress();
                java.util.Date utilDate = dto.getDateOfBirth();
                long sqlLongDate = utilDate.getTime();
                ps.setString(1, empID);
                ps.setString(2, fullName);
                ps.setString(3, phone);
                ps.setString(4, email);
                ps.setString(5, address);
                ps.setLong(6, sqlLongDate);
                int row = ps.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public List<EmpDTO> getAllEmp() throws ClassNotFoundException, SQLException, ParseException {
        List<EmpDTO> list = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select empID, fullname, phone, email, address, dateOfBirth From tblEmployees Where isDelete = 0";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    String empID = rs.getString("empID");
                    String fullName = rs.getString("fullname");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
                    long sqlDate = rs.getLong("dateOfBirth");
                    java.util.Date utilDate = new java.util.Date(sqlDate);
                    EmpDTO dto = new EmpDTO(empID, fullName, phone, email, address, utilDate); 
                    //do chỉ load những dto có isDelete là true nên để là true luôn
                    list.add(dto);
                }
                return list;
            }
            return null;

        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }

    }

    public EmpDTO getEmpByID(String id) throws ClassNotFoundException, SQLException, ParseException, Exception {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select fullname, phone, email, address, dateOfBirth From tblEmployees Where empID = ? and isDelete = 0";
                ps = con.prepareStatement(sql);
                ps.setString(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String fullName = rs.getString("fullname");
                    String phone = rs.getString("phone");
                    String email = rs.getString("email");
                    String address = rs.getString("address");
//                    java.sql.Date sqlDate = rs.getDate("dateOfBirth");
//                    java.util.Date utilDate = new java.util.Date(sqlDate.getTime());

                    long sqlDate = rs.getLong("dateOfBirth");
                    java.util.Date utilDate = new java.util.Date(sqlDate);

                    EmpDTO dto = new EmpDTO(id, fullName, phone, email, address, utilDate);
                    return dto;
                }
            }
            return null;

        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean updateEmp(EmpDTO dto) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {

                String sql = "Update tblEmployees Set fullname = ?, phone = ?, email = ?, "
                        + "address = ?, dateOfBirth = ? Where empID = ? and isDelete = 0 ";
                ps = con.prepareStatement(sql);
                String empID = dto.getEmpID();
                String fullName = dto.getFullName();
                String phone = dto.getPhone();
                String email = dto.getEmail();
                String address = dto.getAddress();
                java.util.Date utilDate = dto.getDateOfBirth();
                long sqlDate = utilDate.getTime();
                ps.setString(1, fullName);
                ps.setString(2, phone);
                ps.setString(3, email);
                ps.setString(4, address);
                ps.setLong(5, sqlDate);
                ps.setString(6, empID);
                //executeUpdate
                int row = ps.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
            return false;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean removeEmp(EmpDTO dto) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Update tblEmployees Set isDelete = 1 Where  empID = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getEmpID());
                int row = ps.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
            return false;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }

    public boolean recoveryEmp(EmpDTO dto) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Update tblEmployees Set isDelete = 0 Where empID = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getEmpID());

                int row = ps.executeUpdate();

                if (row > 0) {
                    return true;
                }
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public boolean checkDuplicateEmpID(String id) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {

        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public boolean empIDRegex(String empID) {
        String regex = "[a-zA-Z0-9]{1,10}";
        boolean isOK = empID.matches(regex);
        return isOK;
    }

    public boolean fullNameRegex(String fullName) {
        if (fullName.isEmpty() || fullName.length() > 30) {
            return false;
        }
        return true;
    }

    public boolean phoneRegex(String phone) {
        String regex = "[0-9]{1,15}";
        boolean isOK = phone.matches(regex);
        return isOK;
    }

    public boolean emailRegex(String email) {
        String regex = "[a-zA-Z0-9]+@[a-zA-Z0-9]+[.][a-zA-Z0-9]+";
        boolean isOK = email.matches(regex);
        return isOK;
    }

    public boolean addressRegex(String address) {
        if (address.isEmpty() || address.length() > 300) {
            return false;
        }
        return true;
    }

    public boolean dateRegex(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        java.util.Date date = format.parse(dateString);
        java.util.Date now = new java.util.Date();

        if (date.getTime() > now.getTime()) {
            return false;
        }
        return true;
    }
}
