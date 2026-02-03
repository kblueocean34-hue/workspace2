export type Category = {
  id: number;
  name: string;
  children?: Category[];
};

export const categories: Category[] = [
  {
    id: 1,
    name: "음료",
    children: [
      { id: 11, name: "커피" },
      { id: 12, name: "티" },
      { id: 13, name: "주스" },
    ],
  },
  {
    id: 2,
    name: "디저트",
    children: [
      { id: 21, name: "케이크" },
      { id: 22, name: "쿠키" },
    ],
  },
];
