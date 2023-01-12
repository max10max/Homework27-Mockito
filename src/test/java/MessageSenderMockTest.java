import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import static org.mockito.BDDMockito.given;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

public class MessageSenderMockTest {

    private final String ip = "172.123.12.19";

    MessageSender messageSender;
    GeoService geoService = Mockito.mock(GeoService.class);
    LocalizationService localizationService = Mockito.mock(LocalizationService.class);

    Map<String, String> map = new HashMap<>();


    @Test
    public void messageRuTest(){

        given(geoService.byIp("172.123.12.19")).willReturn(new Location(null, Country.RUSSIA, null, 0));
        given(localizationService.locale(Country.RUSSIA)).willReturn("Добро пожаловать");

        messageSender = new MessageSenderImpl(geoService, localizationService);
        map.put("x-real-ip", "172.123.12.19");
        String preferences = messageSender.send(map);
        Assertions.assertTrue(preferences.startsWith("Добро"));
    }

    @Test
    public void messageEngTest(){

        given(geoService.byIp("96.123.12.19")).willReturn(new Location(null, Country.USA, null, 0));
        given(localizationService.locale(Country.USA)).willReturn("Welcome");

        messageSender = new MessageSenderImpl(geoService, localizationService);
        map.put("x-real-ip", "96.123.12.19");
        String preferences = messageSender.send(map);
        Assertions.assertTrue(preferences.startsWith("Wel"));
    }

    @Test
    public void locationByIpTest(){
        GeoService geoService1 = Mockito.spy(GeoServiceImpl.class);
        Assertions.assertEquals(Country.RUSSIA, geoService1.byIp("172.").getCountry());
        Assertions.assertEquals(Country.USA, geoService1.byIp("96.").getCountry());
    }

    @Test
    public  void localeTest(){
        LocalizationService localizationService1 = Mockito.spy(LocalizationServiceImpl.class);
        Assertions.assertEquals("Добро пожаловать", localizationService1.locale(Country.RUSSIA));
        Assertions.assertEquals("Welcome", localizationService1.locale(Country.USA) );
    }


}
