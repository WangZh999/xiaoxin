#!/usr/bin/env python
import random
import sys

import requests
import json

headers = {
    'authority': 'zuoyenew.xinkaoyun.com:30001',
    'sec-ch-ua': '" Not;A Brand";v="99", "Google Chrome";v="91", "Chromium";v="91"',
    'accept': 'application/json, text/javascript, */*; q=0.01',
    'dnt': '1',
    'sec-ch-ua-mobile': '?0',
    'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
    'content-type': 'application/x-www-form-urlencoded; charset=UTF-8',
    'origin': 'https://homework.xinkaoyun.com',
    'sec-fetch-site': 'same-site',
    'sec-fetch-mode': 'cors',
    'sec-fetch-dest': 'empty',
    'referer': 'https://homework.xinkaoyun.com/',
    'accept-language': 'zh,en-US;q=0.9,en;q=0.8'
}

cookies = requests.cookies.RequestsCookieJar()

global token


def login():
    url = "https://zuoyenew.xinkaoyun.com:30001/holidaywork/login"
    payload = "userName=18012345678&userPass=abc123&platform=pc&deviceNo=Pc_Hello"
    response = requests.request("POST", url, headers=headers, data=payload, cookies=cookies)
    cookies.update(response.cookies)
    return response


def get_token(resp):
    json_res = json.loads(resp.text)
    global token
    try:
        token = json_res.get('data').get('token')
    except Exception:
        print(resp.text)
        exit(1)


def get_teacher_class():
    url = "https://zuoyenew.xinkaoyun.com:30001/holidaywork/teacher/getTeacherClass"
    payload = "token=" + token
    response = requests.request("POST", url, headers=headers, data=payload, cookies=cookies)
    json_res = json.loads(response.text)
    class_ids = []
    for cl in json_res.get('data'):
        class_ids.append(cl.get('classId'))

    return class_ids


def get_student_tasks(class_id):
    url = "https://zuoyenew.xinkaoyun.com:30001/holidaywork/teacher/getStudentTasks"
    payload = "classId=" + str(class_id) + "&sid=3&token=" + token
    response = requests.request("POST", url, headers=headers, data=payload, cookies=cookies)
    json_res = json.loads(response.text)
    task_ids = []
    for cl in json_res.get('data'):
        task_ids.append(cl.get('taskId'))

    return task_ids


def get_student_correct_state(class_id, task_id):
    url = "https://zuoyenew.xinkaoyun.com:30001/holidaywork/teacher/getStudentCorrectState"
    payload = "page=1&limit=99999999&classId=" + str(class_id) + "&taskId=" + str(task_id) + "&token=" + token
    response = requests.request("POST", url, headers=headers, data=payload, cookies=cookies)
    json_res = json.loads(response.text)
    print(response.text)
    u_ids = []
    for cl in json_res.get('data'):
        if cl.get('isAssess') == 0 or cl.get('correctType') == 0:
            u_ids.append(cl.get('userId'))
    return u_ids


def assess_student_task(task_id, user_id):
    url = "https://zuoyenew.xinkaoyun.com:30001/holidaywork/teacher/assessStudentTask"
    payload = "taskId=" + str(task_id) + "&userId=" + str(user_id) + "&taskAssess=4%2C4%2C3%2C4&assessContent=excellent" + "&token=" + token
    print(payload)
    response = requests.request("POST", url, headers=headers, data=payload, cookies=cookies)
    # print(response.text)


def correct_student_task(task_id, user_id, tea_id, tea_score):
    score = random.randint(int(tea_score * 0.7), int(tea_score))
    url = "https://zuoyenew.xinkaoyun.com:30001/holidaywork/teacher/correctStudentTask"
    payload = "taskId=" + str(task_id) + "&userId=" + str(user_id) + "&token=" + token + "&taskScore=" + str(tea_id) + "-" + str(score) + "-" + str(tea_score)
    print(payload)
    response = requests.request("POST", url, headers=headers, data=payload, cookies=cookies)
    print(response.text)


def get_student_subjective_answer(taskId, userId):
    url = "https://zuoyenew.xinkaoyun.com:30001/holidaywork/teacher/getStudentSubjectiveAnswer"
    payload = "taskId=" + str(taskId) + "&userId=" + str(userId) + "&token=" + token
    response = requests.request("POST", url, headers=headers, data=payload, cookies=cookies)
    json_res = json.loads(response.text)
    answer = json_res.get("answers")[0]
    tea_id = answer.get("teaId")
    tea_score = answer.get("teaScore")
    print("teaId: " + str(tea_id) + " ;teaScore: " + str(tea_score))
    return tea_id, tea_score


if __name__ == '__main__':
    arg_num = len(sys.argv)
    login_resp = login()
    get_token(login_resp)  # login
    print("Login success. Token: " + token)
    for class_id in get_teacher_class():
        task_ids = get_student_tasks(class_id)
        for task_id in task_ids:
            students = get_student_correct_state(class_id, task_id)
            if len(students) <= 0:
                continue
            teaId, teaScore = get_student_subjective_answer(task_id, students[0])
            for student in students:
                assess_student_task(task_id, student)
                if arg_num > 1:
                    correct_student_task(task_id, student, teaId, teaScore)
