package com.mgs.pims.web.retriever;

import com.mgs.pims.annotations.PimsEntity;
import com.mgs.pims.types.retriever.PimsRetriever;
import com.mgs.pims.web.data.PersonData;
import com.mgs.pims.web.persistable.Person;

import java.util.List;

@PimsEntity
public interface PersonRetriever extends PimsRetriever<PersonData, Person> {
    List<Person> byName(String name);
}
