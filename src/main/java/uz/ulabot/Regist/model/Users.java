package uz.ulabot.Regist.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "tg_id")
    private String tgId;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "code")
    private String code;

    @Column(name = "step")
    private String step;
}
