package cn.chao.maven.entity;

public class RolesMenusEntityKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_roles_menus.menu_id
     *
     * @mbg.generated
     */
    private Long menuId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_roles_menus.role_id
     *
     * @mbg.generated
     */
    private Long roleId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_roles_menus.menu_id
     *
     * @return the value of tb_roles_menus.menu_id
     *
     * @mbg.generated
     */
    public Long getMenuId() {
        return menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_roles_menus.menu_id
     *
     * @param menuId the value for tb_roles_menus.menu_id
     *
     * @mbg.generated
     */
    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_roles_menus.role_id
     *
     * @return the value of tb_roles_menus.role_id
     *
     * @mbg.generated
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_roles_menus.role_id
     *
     * @param roleId the value for tb_roles_menus.role_id
     *
     * @mbg.generated
     */
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}