package SMTP;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
public class EmailRequest {
    private String to;
    private String subject;
    private String text;
}