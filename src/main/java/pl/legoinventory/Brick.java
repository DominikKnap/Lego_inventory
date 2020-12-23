package pl.legoinventory;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "bricks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class Brick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String number;
}
