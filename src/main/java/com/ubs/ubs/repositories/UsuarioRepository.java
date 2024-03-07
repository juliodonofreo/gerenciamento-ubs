package com.ubs.ubs.repositories;

import com.ubs.ubs.entities.User;
import com.ubs.ubs.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = true, value = """
	SELECT tb_usuario.email AS username, tb_usuario.senha, tb_funcao.id AS roleId, tb_funcao.authority
	FROM tb_usuario
	INNER JOIN tb_usuario_funcao ON tb_usuario.id = tb_usuario_funcao.usuario_id
	INNER JOIN tb_funcao ON tb_funcao.id = tb_usuario_funcao.funcao_id
	WHERE tb_usuario.email = :email
	""")
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);
}
