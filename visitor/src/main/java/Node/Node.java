package Node;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonFormat(pattern = JsonFormat.DEFAULT_LOCALE)
@JsonInclude(JsonInclude.Include.NON_NULL)

public interface Node {

}
