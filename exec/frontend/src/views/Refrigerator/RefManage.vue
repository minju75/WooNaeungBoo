<template>
    <v-container>
        <div class="outBox listBox">
            <p class="titleText">
                {{ ref_name }}
            </p>
            <v-row class="box">
                <div>
                    <drop class="drop list listBox ingredList">
                        <drag
                            v-for="(ing, index) in addList"
                            :key="index"
                            :class="{ [ing]: true }"
                            :transfer-data="{ item: ing, list: addList, example: 'list' }"
                            class="font-18"
                            >{{ ing.ingredientsdetail.name }}</drag
                        >
                    </drop>
                    <v-row class="refBtnBox">
                        <v-dialog v-model="addDialog">
                            <template v-slot:activator="{ on, attrs }">
                                <div v-bind="attrs" v-on="on" class="ingrePlusBtn">
                                    <img :src="plusBtn" alt="추가버튼" />
                                </div>
                            </template>
                            <v-card>
                                <v-card-title>재료를 추가해주세요!</v-card-title>
                                <v-card-text>
                                    <v-select
                                        :items="category"
                                        v-model="addIngredients.ingredientsdetail.category"
                                        label="Solo field"
                                        solo
                                        @change="getIngredientsName()"
                                    ></v-select>
                                    <v-select
                                        :items="ingredientsName"
                                        v-model="addIngredients.ingredientsdetail.name"
                                        label="Solo field"
                                        solo
                                        @change="selectId(addIngredients.ingredientsdetail.name)"
                                    ></v-select>
                                    <p>유통기한</p>
                                    <input type="date" v-model="addIngredients.expired" />
                                </v-card-text>
                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn color="primary" text @click="addIngred()">
                                        추가완료
                                    </v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-dialog>
                        <v-spacer></v-spacer>
                        <v-dialog v-model="minusDialog">
                            <template v-slot:activator="{ on, attrs }">
                                <div v-bind="attrs" v-on="on" class="ingreMinusBtn">
                                    <img :src="minusBtn" alt="뺴기버튼" />
                                </div>
                            </template>
                            <v-card>
                                <v-card-title>재료를 빼주세요!</v-card-title>
                                <v-card-text>
                                    <v-select
                                        :items="category"
                                        v-model="removeCate"
                                        label="Solo field"
                                        solo
                                        @change="getRemoveIngredientsName()"
                                    ></v-select>
                                    <v-select
                                        :items="myName"
                                        v-model="removeName"
                                        label="Solo field"
                                        solo
                                        @change="selectRemoveId(removeName)"
                                    ></v-select>
                                </v-card-text>
                                <v-card-actions>
                                    <v-spacer></v-spacer>
                                    <v-btn color="primary" text @click="removeIngred(removeId)">
                                        빼기완료
                                    </v-btn>
                                </v-card-actions>
                            </v-card>
                        </v-dialog>
                    </v-row>
                </div>
                <v-row>
                    <div v-for="(list, i) in lists" :key="i" class="drop">
                        <p class="fridgeTitleBox">FRIDGE</p>
                        <div v-for="(inlist, ind) in list" :key="ind" class="ingredientsBox">
                            <drop
                                class="dropBox"
                                @drop="handleDrop(inlist, ...arguments, ind, i, inlist)"
                            >
                                <drag
                                    v-for="(ininlist, inde) in inlist"
                                    :key="inde"
                                    :transfer-data="{
                                        item: ininlist,
                                        list: inlist,
                                        example: 'list',
                                    }"
                                >
                                    <v-row class="ingredientsList">
                                        <v-spacer></v-spacer>
                                        <div class="ingredImg">
                                            <img
                                                :src="
                                                    require(`@/assets/images/ingredients/${ininlist.ingredientsdetail.image}`)
                                                "
                                                alt="재료이미지"
                                            />
                                        </div>
                                        <p class="ingredName">
                                            {{ ininlist.ingredientsdetail.name }}
                                        </p>
                                        <v-spacer></v-spacer>
                                    </v-row>
                                </drag>
                            </drop>
                        </div>
                    </div>
                </v-row>
                <v-spacer></v-spacer>
            </v-row>
        </div>
        <v-row class="explainIngredBox my-7 py-3">
            <v-spacer></v-spacer>
            <p>
                빨간색으로 표시된 재료들은 <br />
                <span>"유통기한이 지난"</span>
                식품입니다.
            </p>
            <v-spacer></v-spacer>
            <v-icon class="expainIngredIcon">mdi-arrow-down-bold-box-outline</v-icon>
            <v-spacer></v-spacer>
        </v-row>
        <div>
            <p class="titleText">유통기한 1주일 미만</p>
            <v-slide-group center-active show-arrows class="garbageBox">
                <v-slide-item v-for="(ingredient, index) in garbages" :key="index" class="itemBox">
                    <v-card
                        class="ma-1"
                        height="100"
                        width="70"
                        @click="goRecipeList(ingredient.ingredientsdetail.id)"
                    >
                        <img
                            :src="
                                require(`@/assets/images/ingredients/${ingredient.ingredientsdetail.image}`)
                            "
                            alt="재료이미지"
                            class="itemImg"
                        />

                        <p class="itemText">
                            {{ ingredient.ingredientsdetail.name }}
                        </p>
                    </v-card>
                </v-slide-item>
            </v-slide-group>
        </div>
        <div class="mt-7">
            <p class="titleText">내 재료 목록</p>
            <v-slide-group center-active show-arrows class="garbageBox">
                <v-slide-item v-for="(ingredient, index) in ingredients" :key="index">
                    <v-card
                        class="ma-1"
                        height="100"
                        width="70"
                        @click="goRecipeList(ingredient.ingredientsdetail.id)"
                    >
                        <img
                            :src="
                                require(`@/assets/images/ingredients/${ingredient.ingredientsdetail.image}`)
                            "
                            alt="재료이미지"
                            class="itemImg"
                        />

                        <p class="itemText">
                            {{ ingredient.ingredientsdetail.name }}
                        </p>
                    </v-card>
                </v-slide-item>
            </v-slide-group>
        </div>
    </v-container>
</template>
<script>
// import http from '../../api/axios.js';
import {
    getCategory,
    getAllIngredients,
    getSomeIngredients,
    getIngredients,
    addIngredients,
    deleteIngredients,
    moveIngredients,
    getFridgeDetail,
} from '../../api/refrigerator.js';
import { moveRecipeList } from '@/api/move.js';
import { Drag, Drop } from 'vue-drag-drop';
import swal from 'sweetalert';
export default {
    components: { Drag, Drop },
    name: 'RefManage',

    data() {
        return {
            addDialog: false,
            minusDialog: false,
            ref_id: 0,
            ref_name: '',
            ref_type: '',
            garbages: [],
            category: ['All'],
            ingredients: Array,
            addIngredients: {
                expired: 'string',
                locx: 0,
                locy: 0,
                fridge: {
                    id: 0,
                },
                ingredientsdetail: {
                    id: 0,
                    category: '',
                    name: '',
                },
            },
            addItem: {
                expired: 'string',
                locx: 10,
                locy: 10,
                fridgeId: 0,
                ingredientsDetailId: 0,
            },
            ingredientsName: [],
            ingredientsId: [],
            lists: [],
            addList: [
                {
                    id: 0,
                    expired: 'string',
                    locx: 0,
                    locy: 0,
                    fridgeId: 0,
                    fridge: {
                        id: '',
                    },
                    ingredientsdetail: {
                        id: '',
                        name: '',
                        category: '',
                    },
                    ingredientsDetailId: 0,
                },
            ],
            temp: [],
            moveItem: {
                id: 0,
                expired: 'string',
                locx: 0,
                locy: 0,
                fridgeId: 0,
                ingredientsDetailId: 0,
            },
            fridgeInfo: [],
            plusBtn: require('@/assets/images/plusBtn.png'),
            minusBtn: require('@/assets/images/minusBtn.png'),
            tempList: [],
            myName: [],
            removeCate: '',
            removeName: '',
            removeId: 0,
            removeItem: [],
        };
    },

    created() {
        this.ref_id = this.$route.params.rid;
        this.addIngredients.fridge.id = this.ref_id;
        this.getFridgeDetail();
        this.getAllCategories();
    },
    methods: {
        getAllCategories() {
            getCategory()
                .then((response) => {
                    this.category = response.data.category;
                    this.category.unshift('All'); // 카테고리 가장 앞에 All 추가.
                })
                .catch((error) => {
                    console.log(error);
                });
        },
        getRemoveIngredientsName() {
            this.myName.splice(0);
            this.ingredientsId.splice(0);

            if (this.removeCate == 'All') {
                // All 일 때 가지고 있는 재료 데이터 추가
                this.ingredients.forEach((el) => {
                    this.myName.push(el.ingredientsdetail.name);
                    this.removeItem.push(el);
                });
            } else {
                this.ingredients.forEach((el) => {
                    // 해당 카테고리에 있는 재료 데이터 추가
                    if (el.ingredientsdetail.category == this.removeCate) {
                        this.myName.push(el.ingredientsdetail.name);
                        this.removeItem.push(el);
                    }
                });
            }
        },
        getIngredientsName() {
            this.ingredientsName.splice(0);
            this.ingredientsId.splice(0);
            let category = this.addIngredients.ingredientsdetail.category;
            if (category == 'All') {
                getAllIngredients().then((response) => {
                    let name = response.data.ingredients;
                    for (var i = 0; i < name.length; i++) {
                        this.ingredientsName.push(name[i].name);
                        this.ingredientsId.push(name[i].id);
                    }
                });
            } else {
                getSomeIngredients(category)
                    .then((response) => {
                        let name = response.data.ingredients;
                        for (var i = 0; i < name.length; i++) {
                            this.ingredientsName.push(name[i].name);
                            this.ingredientsId.push(name[i].id);
                        }
                    })
                    .catch(() => {
                        swal('실패!', {
                            icon: 'error',
                        });
                    });
            }
        },
        getFridgeDetail() {
            getFridgeDetail(this.ref_id)
                .then((response) => {
                    this.fridgeInfo = response.data.fridge;
                    this.ref_name = response.data.fridge.name;
                    this.ref_type = response.data.fridge.type;
                    this.setRefType();
                })
                .catch((error) => console.log(error));
        },
        getIngredients() {
            getIngredients(this.ref_id)
                .then((response) => {
                    this.ingredients = response.data.ingredients;

                    this.ingredients.forEach((el) => {
                        let x = el.locx;
                        let y = el.locy;
                        if (x == 10 || y == 10) {
                            // 아직 위치가 정해지지 않은 경우
                            let temp = {
                                id: el.id,
                                expired: el.expired,
                                fridgeId: el.fridge.id,
                                fridge: {
                                    id: el.fridge.id,
                                },
                                ingredientsDetailId: el.ingredientsdetail.id,
                                locx: el.locx,
                                locy: el.locy,
                                ingredientsdetail: {
                                    id: el.ingredientsdetail.id,
                                    name: el.ingredientsdetail.name,
                                    category: el.ingredientsdetail.category,
                                },
                            };
                            this.addList.push(temp); // addList에 추가.
                        } else {
                            // 아니면 list의 해당위치에 추가.
                            this.lists[y][x].push(el);
                        }
                    });
                    setTimeout(this.checkShelfLife, 100); // 유통기한 체크
                })
                .catch((error) => {
                    console.log(error);
                });
        },
        setRefType() {
            switch (this.ref_type) {
                // 냉장고 타입에 따라 생성할 냉장고 칸을 배열로 만듬.
                case 44:
                    this.lists = [
                        [[], [], [], []],
                        [[], [], [], []],
                    ];
                    break;
                case 55:
                    this.lists = [
                        [[], [], [], [], []],
                        [[], [], [], [], []],
                    ];
                    break;
                case 66:
                    this.lists = [
                        [[], [], [], [], [], []],
                        [[], [], [], [], [], []],
                    ];
                    break;
                case 4444:
                    this.lists = [
                        [[], [], [], []],
                        [[], [], [], []],
                        [[], [], [], []],
                        [[], [], [], []],
                    ];
                    break;
                case 5555:
                    this.lists = [
                        [[], [], [], [], []],
                        [[], [], [], [], []],
                        [[], [], [], [], []],
                        [[], [], [], [], []],
                    ];
                    break;
            }
            if (this.ref_type) {
                if (this.ref_type == 4444 || this.ref_type == 5555) {
                    // 타입이 양문형이면
                    let box = document.querySelector('.box');
                    box.classList.add('fourBox'); // 넓이를 증가시키는 class 추가.
                }
                this.getIngredients();
            }
        },
        checkShelfLife() {
            // 유통기한 검사.
            let current = new Date();

            let year = current.getFullYear();
            let month = current.getMonth() + 1;
            let date7 = current.getDate() + 7;
            let today7 = `${year}-${month}-${date7}`;
            let today7O = new Date(today7);

            for (var i = 0; i < this.ingredients.length; i++) {
                var ingre = new Date(this.ingredients[i].expired);

                if (ingre.getTime() <= today7O.getTime()) {
                    // 일주일 이하인 경우
                    this.garbages.push(this.ingredients[i]);
                }
            }
            setTimeout(this.checkDeadItem, 100);
        },
        checkDeadItem() {
            let current = new Date();

            let year = current.getFullYear();
            let month = current.getMonth() + 1;
            let date = current.getDate() + 1;
            let today = `${year}-${month}-${date}`;
            let todayO = new Date(today);

            let itemBox = document.querySelectorAll('.itemText');
            itemBox.forEach((el) => {
                this.tempList.push(el.innerText);
            });

            for (var i = 0; i < this.ingredients.length; i++) {
                let life = new Date(this.ingredients[i].expired);
                if (life.getTime() <= todayO.getTime()) {
                    // 유통기한이 다 된경우
                    itemBox.forEach((el) => {
                        let text = el.innerText;

                        // 이름을 찾아서 빨간 테두리 표시.
                        if (text == this.ingredients[i].ingredientsdetail.name) {
                            el.classList.add('deadItem');
                        }
                    });
                }
            }
        },
        selectId(name) {
            let idx = this.ingredientsName.indexOf(name);
            this.addIngredients.ingredientsdetail.id = this.ingredientsId[idx];
        },
        selectRemoveId(name) {
            this.removeItem.forEach((el) => {
                if (el.ingredientsdetail.name == name) {
                    this.removeId = el.id;
                }
            });
        },
        addIngred() {
            this.addItem.expired = this.addIngredients.expired;
            this.addItem.fridgeId = this.ref_id;
            this.addItem.ingredientsDetailId = this.addIngredients.ingredientsdetail.id;

            addIngredients(this.addItem)
                .then(() => {
                    this.addDialog = false;
                    let detailId = this.addItem.ingredientsDetailId;

                    this.ingredients.forEach((el) => {
                        if (el.ingredientsdetail.id == detailId) {
                            let temp = {
                                id: el.id,
                                expired: el.expired,
                                fridgeId: el.firdge.id,
                                fridge: {
                                    id: el.fridge.id,
                                },
                                ingredientsDetailId: el.ingredientsdetail.id,
                                locx: el.locx,
                                locy: el.locy,
                                ingredientsdetail: {
                                    id: el.ingredientsdetail.id,
                                    name: el.ingredientsdetail.name,
                                },
                            };

                            this.addList.push(temp);
                        }
                    });
                    window.location.href = `/refmanage/${this.ref_id}`;
                })
                .catch((error) => {
                    swal(error, {
                        icon: 'error',
                    });
                });
        },
        removeIngred(id) {
            deleteIngredients(id)
                .then(() => {
                    swal('삭제 성공!', {
                        icon: 'success',
                    });
                    this.minusDialog = false;
                    window.location.href = `/refmanage/${this.ref_id}`;
                })
                .catch((error) => {
                    swal(error, {
                        icon: 'error',
                    });
                });
        },
        goRecipeList(id) {
            moveRecipeList(id);
        },
        handleDrop(toList, data, event, x, y) {
            this.temp = data.item;

            data.item.locx = x;
            data.item.locy = y;

            toList.push(data.item);
            toList.sort((a, b) => a > b);
            if (data.list) {
                data.list.splice(data.list.indexOf(data.item), 1);
            }

            // 이동한 item의 정보를 moveItem에다 저장해두고,
            this.moveItem.id = this.temp.id;
            this.moveItem.expired = this.temp.expired;
            this.moveItem.locx = x;
            this.moveItem.locy = y;
            this.moveItem.fridgeId = this.temp.fridge.id;
            this.moveItem.ingredientsDetailId = this.temp.ingredientsdetail.id;
            // 이동이 완료된 moveItem을 저장.
            moveIngredients(this.moveItem)
                .then(() => {
                    window.location.href = `/refmanage/${this.ref_id}`;
                })
                .catch((error) => {
                    swal(error, {
                        icon: 'error',
                    });
                });
        },
    },
};
</script>
<style scoped>
@import './../../assets/css/refManage.css';
</style>
