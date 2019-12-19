package group.shkd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producer {
    int id;
    String title;

    @Override
    public String toString() {
        return title;
    }
}
