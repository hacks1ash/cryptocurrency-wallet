package com.hacks1ash.crypto.wallet.core.utils;

import org.bitcoinj.core.*;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptPattern;
import org.bitcoinj.wallet.DeterministicSeed;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@Scope("singleton")
public class MnemonicWords {

  private List<String> words;

  @PostConstruct
  public void buildList() {
    URL resource = getClass().getClassLoader().getResource("words.txt");
    if (resource == null) {
      throw new IllegalArgumentException("file not found!");
    } else {
      try {
        File file = new File(resource.toURI());
        words = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
      } catch (IOException | URISyntaxException e) {
        e.printStackTrace();
      }
    }

  }

  public List<String> getWords() {
    return words;
  }

  public List<String> getRandomWords(int count) {
    List<String> shuffledWords = new ArrayList<>(words);
    Collections.shuffle(shuffledWords);
    return shuffledWords.subList(0, count);
  }
}
