package com.daexsys.automata.worldclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NameGenerator {

    public static String getName() {
        Random random = new Random();

        List<String> names = new ArrayList<>();
        names.add("Intrepid");
        names.add("Peaceful");
        names.add("Ironic");
        names.add("Placid");
        names.add("Firey");
        names.add("Aquatic");
        names.add("Surreal");
        names.add("Cognizant");
        names.add("Capricious");
        names.add("Ambiguous");
        names.add("Ephemeral");
        names.add("Reflective");
        names.add("Snowy");
        names.add("Prodigal");
        names.add("Lamenting");
        names.add("Midnight");
        names.add("Bitwise");
        names.add("Patient");
        names.add("Euphoric");
        names.add("Shrouded");
        names.add("Astral");

        List<String> names2 = new ArrayList<>();
        names2.add("Panda");
        names2.add("Honeycomb");
        names2.add("Skeleton");
        names2.add("Ingot");
        names2.add("Apricot");
        names2.add("Wave");
        names2.add("Ripple");
        names2.add("Mothman");
        names2.add("Paladin");
        names2.add("Arachnid");
        names2.add("Shimmer");
        names2.add("Lemon");
        names2.add("Wanderlust");
        names2.add("Meme");
        names2.add("Fish");
        names2.add("Witch");
        names2.add("Cryptid");
        names2.add("Oracle");
        names2.add("Automaton");

        return names.get(random.nextInt(names.size())) + names2.get(random.nextInt((names2.size())));
    }

    public static void main(String[] args) {
        while(true) {
            System.out.println(getName());
        }
    }
}
