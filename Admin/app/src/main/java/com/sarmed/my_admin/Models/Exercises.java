package com.sarmed.my_admin.Models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Exercises implements Serializable {
   private String id;
   String imGif , exerciseName , metValue , descriptionMeal ;

   public Exercises(String imGif, String exerciseName, String metValue, String descriptionMeal) {
      this.imGif = imGif;
      this.exerciseName = exerciseName;
      this.metValue = metValue;
      this.descriptionMeal = descriptionMeal;
   }

   public Exercises(String id, String imGif, String exerciseName, String metValue, String descriptionMeal) {
      this.id = id;
      this.imGif = imGif;
      this.exerciseName = exerciseName;
      this.metValue = metValue;
      this.descriptionMeal = descriptionMeal;
   }

   public Exercises() {
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getImGif() {
      return imGif;
   }

   public void setImGif(String imGif) {
      this.imGif = imGif;
   }

   public String getExerciseName() {
      return exerciseName;
   }

   public void setExerciseName(String exerciseName) {
      this.exerciseName = exerciseName;
   }

   public String getMetValue() {
      return metValue;
   }

   public void setMetValue(String metValue) {
      this.metValue = metValue;
   }

   public String getDescriptionMeal() {
      return descriptionMeal;
   }

   public void setDescriptionMeal(String descriptionMeal) {
      this.descriptionMeal = descriptionMeal;
   }
}
