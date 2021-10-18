export class Item {
  constructor(type: number) { this.type = type; }
  readonly type: number;
  id: number;
  position: number;
}

export class ItemText extends Item {
  constructor(position: number, type: number = 0) {
    super(type);
    this.id = undefined;
    this.position = position;
    this.text = '';
  }
  text: string;
}

export class ItemHttp extends ItemText {
  constructor(position: number) { super(position, 1); this.url = ''; }
  url: string;
}

export class Template {
    id: number;
    name: string;
    items: Item[];
}
