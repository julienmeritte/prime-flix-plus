import { UserRole } from "enums/UserRole";
import { Profile } from "models/profile";

export class User {
  id: number = -1;
  mail: string = "";
  profiles: Profile[] = [];
  role: UserRole = UserRole.ROLE_USER;
  enabled: boolean = false;
  created: Date = new Date();
  updated: Date = new Date();
}
