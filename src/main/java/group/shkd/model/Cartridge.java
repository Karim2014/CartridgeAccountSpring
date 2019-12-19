package group.shkd.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Cartridge {
    int id;
    Producer producer;
    String name = "";
    String num = "";
    State state;
    String note = "";

    public String getFullName() {
        return producer + " " + name;
    }
}
