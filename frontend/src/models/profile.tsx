export class Profile {
  id: number = -1;
  pseudo: string = "";
  isMainProfile: boolean = false;
  isYoung: boolean = false;
  image: string = "";
  receiveNewsletter: boolean = false;
  receiveNewSeries: boolean = false;
  receiveNewFilms: boolean = false;
  receiveNewSeasons: boolean = false;
  created: Date = new Date();
  updated: Date = new Date();
}
