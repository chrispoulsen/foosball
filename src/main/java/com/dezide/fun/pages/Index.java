package com.dezide.fun.pages;

import com.dezide.fun.backend.dao.PersonDao;
import com.dezide.fun.backend.services.MatchService;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Start page of application foosball.
 */
public class Index
{

    private @Inject
    PersonDao personDao;

    private @Inject MatchService matchService;

    @Property
    private String winner1;

    @Property
    private String winner2;

    @Property
    private String looser1;

    @Property
    private String looser2;

    @InjectComponent
    private Zone matchZone;

    public List<String> onProvideCompletionsFromWinner1(String partial, String extra)
    {
        JSONObject jsonObject = new JSONObject(extra);

        List<String> exclusions = setupExclusionList(   jsonObject.getString("winner2"),
                                                        jsonObject.getString("looser1"),
                                                        jsonObject.getString("looser2"));
        return personDao.getAutoCompleteSuggestions(partial, exclusions);
    }

    public Object onAutocompleteselectfromwinner1(String value) {
        winner1 = value;
        return matchZone.getBody();
    }

    public List<String> onProvideCompletionsFromWinner2(String partial, String extra)
    {
        JSONObject jsonObject = new JSONObject(extra);

        List<String> exclusions = setupExclusionList(   jsonObject.getString("winner1"),
                jsonObject.getString("looser1"),
                jsonObject.getString("looser2"));
        return personDao.getAutoCompleteSuggestions(partial, exclusions);
    }

    public Object onAutocompleteselectfromwinner2(String value) {
        winner2 = value;
        return matchZone.getBody();
    }

    public List<String> onProvideCompletionsFromLooser1(String partial, String extra)
    {
        JSONObject jsonObject = new JSONObject(extra);

        List<String> exclusions = setupExclusionList(   jsonObject.getString("winner1"),
                jsonObject.getString("winner2"),
                jsonObject.getString("looser2"));
        return personDao.getAutoCompleteSuggestions(partial, exclusions);
    }

    public Object onAutocompleteselectfromlooser1(String value) {
        looser1 = value;
        return matchZone.getBody();
    }

    public List<String> onProvideCompletionsFromLooser2(String partial, String extra)
    {
        JSONObject jsonObject = new JSONObject(extra);

        List<String> exclusions = setupExclusionList(   jsonObject.getString("winner1"),
                jsonObject.getString("winner2"),
                jsonObject.getString("looser1"));
        return personDao.getAutoCompleteSuggestions(partial, exclusions);
    }

    public Object onAutocompleteselectfromlooser2(String value) {
        looser2 = value;
        return matchZone.getBody();
    }

    private @Inject Request request;


    Object onSuccess() {
        matchService.recordMatchResult(winner1, winner2, looser1, looser2);

        return request.isXHR() ? matchZone.getBody() : null;
    }


    private List<String> setupExclusionList(String ... args ) {
        List<String> res = new ArrayList<String>();

        for(String exclusion: args) {
            if( isNotNullOrEmpty(exclusion) ) res.add(exclusion);
        }
        return res;
    }

    private boolean isNotNullOrEmpty(String testVal) {
        return testVal != null && !"".equals(testVal);
    }


}
