package see.integration;

import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import see.See;
import see.parser.config.ConfigBuilder;
import see.parser.config.GrammarConfiguration;
import see.parser.config.LocalizedBigDecimalFactory;
import see.parser.numbers.NumberFactory;
import see.tree.Node;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;

@RunWith(Theories.class)
public class ProductionConditionsTest {

    See see;

    @Before
    public void setUp() throws Exception {
        Locale locale = new Locale("ru", "RU");
        NumberFactory numberFactory = new LocalizedBigDecimalFactory(locale);
        GrammarConfiguration config = ConfigBuilder.defaultConfig().setNumberFactory(numberFactory).build();

        see = new See(config);
    }

    @DataPoints
    public static final String[] conditions = {
            "index(\"Limit_Logic\";\"L3\";CollType)==2&&!isDefined(PDRate)",
            "isDefined(PDRate)&&index(\"Limit_Logic\";\"L3\";CollType)==2",
            "DaysToMaturity<=30&&DaysToMaturity>=0&&isDefined(PD)",
            "index(\"Limit_Logic\";\"L3\";CollType)==2&&isDefined(PDRate)",
            "!isDefined(PDRate)&&index(\"Limit_Logic\";\"L3\";CollType)==1",
            "isDefined(Rating)",
            "index(\"Limit_Logic\";\"L3\";CollType)==1&&!isDefined(PDRate)",
            "!isDefined(PDRate)&&index(\"Limit_Logic\";\"L3\";CollType)==2",
            "LoanMode==\"Guarantee\"||LoanMode==\"Unpaid_LC\"",
            "LoanMode!=\"Guarantee\"&&LoanMode!=\"Unpaid_LC\"",
            "index(\"Limit_Logic\";\"L3\";CollType)==1&&Assessment>EAD",
            "1==1",
            "isDefined(PDRate)&&index(\"Limit_Logic\";\"L3\";CollType)==1",
            "!isDefined(PDRate)&&index(\"Limit_Logic\";\"L3\";CollType)!=1&&index(\"Limit_Logic\";\"L3\";CollType)!=2",
            "Regime==2",
            "Regime!=0&&Regime!=2",
            "index(\"Limit_Logic\";\"L3\";CollType)==1&&isDefined(PDRate)",
            "Assessment>EAD&&index(\"Limit_Logic\";\"L3\";CollType)==1",
            "DaysToMaturity>180&&isDefined(PD)",
            "Assessment<=EAD||index(\"Limit_Logic\";\"L3\";CollType)!=1",
            "isDefined(EAD)==0",
            "DaysToMaturity<=180&&DaysToMaturity>90&&isDefined(PD)",
            "isDefined(PD)==0",
            "index(\"Limit_Logic\";\"L3\";CollType)!=1&&index(\"Limit_Logic\";\"L3\";CollType)!=2",
            "isDefined(PDRate)&&index(\"Limit_Logic\";\"L3\";CollType)!=1&&index(\"Limit_Logic\";\"L3\";CollType)!=2",
            "isDefined(CollType)",
            "isDefined(CollType)==0",
            "index(\"Limit_Logic\";\"L3\";CollType)!=1||Assessment<=EAD",
            "Regime==0",
            "isDefined(PD)==1",
            "isDefined(PDRate)",
            "isDefined(EAD)==1",
            "DaysToMaturity<=90&&DaysToMaturity>30&&isDefined(PD)",
            "isDefined(Rating)==0"
    };


    @Theory
    public void testRecognition(String input) throws Exception {
        Node<Object> node = see.parseExpression(input);
        assertNotNull(node);
    }
}
