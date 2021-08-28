package com.hacks1ash.crypto.wallet.core.utils;

import co.elastic.apm.api.CaptureSpan;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Scope("singleton")
public class MnemonicWords {

  private List<String> words;

  @PostConstruct
  public void buildList() throws IOException {
    InputStream resource = new ClassPathResource("words.txt").getInputStream();
    try ( BufferedReader reader = new BufferedReader(
      new InputStreamReader(resource)) ) {
      words = reader.lines().collect(Collectors.toList());
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  @CaptureSpan
  public List<String> getWords() {
    return words;
  }

  @CaptureSpan
  public List<String> getRandomWords(int count) {
    List<String> shuffledWords = new ArrayList<>(words);
    Collections.shuffle(shuffledWords);
    return shuffledWords.subList(0, count);
  }
}
