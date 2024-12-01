import internal from "stream";
import { Format } from "./format";

export class Video {
  id: number = -1;
  name: string = "";
  description: string = "";
  creator: string = "";
  distribution: string = "";
  season: number = -1;
  age: string = "YOUNG";
  date: string = "";
  image: string = "";
  formats: Format[] = [];
  categorie: string[] = [""];
  created: Date = new Date();
  updated: Date = new Date();
}
