package spring.testMixers;

import com.mgs.pims.annotations.PimsEvent;
import com.mgs.pims.annotations.PimsMixer;

import java.util.Map;

import static com.mgs.pims.core.PimsEventType.INPUT_TRANSLATION;

@PimsMixer
public class WithEventsManager {
    @PimsEvent(type = INPUT_TRANSLATION)
    public Map<String, Object> translate(Map<String, Object> input) {
        input.put("name", ((String) input.get("name")).toLowerCase());
        return input;
    }
}
