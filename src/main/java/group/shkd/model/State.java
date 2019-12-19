package group.shkd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class State {
    int id;
    String title;

    @Override
    public String toString() {
        return title;
    }
}
