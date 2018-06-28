package com.theupquark.wow.adapters;

import com.theupquark.wow.models.Achievement;
import com.theupquark.wow.mongo.AchievementStore;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * Test methods for managing achievements
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class WowAchievementAdapterTest {

  private WowAchievementAdapter wowAchievementAdapter;
  private AchievementStore achievementStore = Mockito.mock(AchievementStore.class);

  /**
   * setup
   */
  @Before
  public void setup() {
    this.wowAchievementAdapter = new WowAchievementAdapter(this.achievementStore);
  }

  /**
   * Test getting reduced set of achievements
   */
  @Test
  public void obtainDuplicates_ManyChar() {
    Achievement match1 = new Achievement();
    match1.setId("111111");
    match1.setTime("101010010101");

    Achievement match2 = new Achievement();
    match1.setId("222222");
    match1.setTime("101111010101");

    Achievement unique1 = new Achievement();
    unique1.setId("333333");
    Achievement unique2 = new Achievement();
    unique1.setId("444444");
    Achievement unique3 = new Achievement();
    unique1.setId("555555");
    Achievement unique4 = new Achievement();
    unique1.setId("666666");

    List<Achievement> fromChar1 = new ArrayList<>();
    fromChar1.add(match1);
    fromChar1.add(match2);
    fromChar1.add(unique1);

    List<Achievement> fromChar2 = new ArrayList<>();
    fromChar2.add(unique2);
    fromChar2.add(match1);
    fromChar2.add(match2);

    List<Achievement> fromChar3 = new ArrayList<>();
    fromChar2.add(unique3);
    fromChar2.add(match1);
    fromChar2.add(match2);

    List<Achievement> fromChar4 = new ArrayList<>();
    fromChar2.add(unique4);
    fromChar2.add(match1);
    fromChar2.add(match2);

    List<List<Achievement>> listOfLists = new ArrayList<>();
    listOfLists.add(fromChar1);
    listOfLists.add(fromChar2);
    
    List<Achievement> result = 
      this.wowAchievementAdapter.obtainDuplicates(listOfLists);

    Assert.assertEquals(2, result.size());
    Assert.assertTrue(result.contains(match1));
    Assert.assertTrue(result.contains(match2));
  }

}
